package org.m_tabarkevych.kmpmaps.features.map.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSCoordinateBounds
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMutablePath
import cocoapods.GoogleMaps.GMSPolyline
import cocoapods.GoogleMaps.animateToCameraPosition
import cocoapods.GoogleMaps.animateWithCameraUpdate
import cocoapods.GoogleMaps.kGMSTypeHybrid
import cocoapods.GoogleMaps.kGMSTypeNormal
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapAction
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.darwin.NSObject
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import cocoapods.GoogleMaps.GMSMarker.Companion.markerImageWithColor
import platform.UIKit.UIColor


@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun GoogleMaps(
    isSatelliteView: Boolean,
    theme: Theme,
    isMyLocationEnabled: Boolean,
    userLocation: Coordinates?,
    currentMarker: MarkerUi?,
    markers: List<MarkerUi>,
    routeInfo: RouteInfo?,
    mapActionSharedFlow: SharedFlow<MapAction>,
    onMapClicked: () -> Unit,
    onMapLongClicked: (Coordinates) -> Unit,
    onMarkerClicked: (MarkerUi) -> Unit,
) {
    val mapView = remember { GMSMapView() }
    var mapState by remember { mutableStateOf(CurrentMapState()) }

    LaunchedEffect(Unit) {
        val cameraPosition = GMSCameraPosition.cameraWithLatitude(
            latitude = 50.450001,
            longitude = 30.523333,
            zoom = 13.0f
        )
        mapView.animateToCameraPosition(cameraPosition)
    }

    LaunchedEffect(Unit) {
        mapActionSharedFlow.collectLatest { action ->
            when (action) {
                is MapAction.ZoomToLocation -> {
                    AppLogger.i("Google maps", "ZoomToLocation")
                    val cameraPosition = GMSCameraPosition.cameraWithLatitude(
                        latitude = action.location.lat,
                        longitude = action.location.lng,
                        zoom = 13.0f
                    )
                    mapView.animateToCameraPosition(cameraPosition)
                }

                is MapAction.ZoomToBounds -> {
                    if (action.bounds.isNotEmpty()) {
                        val latitudes = action.bounds.map { it.lat }
                        val longitudes = action.bounds.map { it.lng }

                        val southwest = CLLocationCoordinate2DMake(
                            latitudes.minOrNull() ?: return@collectLatest,
                            longitudes.minOrNull() ?: return@collectLatest
                        )
                        val northeast = CLLocationCoordinate2DMake(
                            latitudes.maxOrNull() ?: return@collectLatest,
                            longitudes.maxOrNull() ?: return@collectLatest
                        )

                        val bounds = GMSCoordinateBounds(southwest, northeast)
                        val cameraUpdate = GMSCameraUpdate.fitBounds(bounds, 50.0)
                        mapView.animateWithCameraUpdate(cameraUpdate)
                    }
                }
            }
        }

    }

    LaunchedEffect(currentMarker, markers, routeInfo) {
        mapState = refreshMapElements(mapView, currentMarker, markers, routeInfo, mapState)
    }

    val type = if (isSatelliteView) kGMSTypeHybrid else kGMSTypeNormal
    mapView.mapType = type


    val delegate = remember {
        object : NSObject(), GMSMapViewDelegateProtocol {

            override fun mapView(mapView: GMSMapView, willMove: Boolean) {
                if (willMove) onMapClicked.invoke()
            }

            override fun mapView(
                mapView: GMSMapView,
                didLongPressAtCoordinate: CValue<CLLocationCoordinate2D>
            ) {
                didLongPressAtCoordinate.useContents {
                    onMapLongClicked.invoke(Coordinates(this.latitude, this.longitude))
                }
            }

            override fun mapView(
                mapView: GMSMapView,
                @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                didTapMarker: GMSMarker
            ): Boolean {
                val userData = didTapMarker.userData()
                AppLogger.i("didTapMarker", "map marker click ${userData}")

                (userData as? MarkerUi)?.run { onMarkerClicked.invoke(userData) }
                return true
            }


        }
    }
    mapView.delegate = delegate

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.settings.setAllGesturesEnabled(true)
            view.settings.setScrollGestures(true)
            view.settings.setZoomGestures(true)
            view.settings.setCompassButton(false)

            view.myLocationEnabled = true // show the users dot
            view.userInteractionEnabled = true
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        )
    )
}


@OptIn(ExperimentalForeignApi::class)
fun refreshMapElements(
    mapView: GMSMapView,
    currentMarker: MarkerUi?,
    markers: List<MarkerUi>,
    routeInfo: RouteInfo?,
    currentMapState: CurrentMapState
): CurrentMapState {
    AppLogger.i("RefreshMapElements", currentMapState.toString())

    var targetMarkerGms: GMSMarker? = null
    val markersGms: MutableList<GMSMarker> = mutableListOf()

    currentMapState.targetMarkerGms?.setMap(null)
    currentMapState.markersGms.forEach { it.setMap(null) }
    currentMapState.route?.setMap(null)


    if (currentMarker != null) {
        targetMarkerGms = GMSMarker().apply {
            userData = currentMarker
            icon = markerImageWithColor(UIColor.blueColor())
            setPosition(
                CLLocationCoordinate2DMake(currentMarker.latitude, currentMarker.longitude)
            )
            map = mapView
        }
    }


    markersGms.addAll(markers.map { marker ->
        GMSMarker().apply {
            userData = marker
            icon = markerImageWithColor(UIColor.blueColor())
            setPosition(CLLocationCoordinate2DMake(marker.latitude, marker.longitude))
            map = mapView
        }
    })

    val polyline = routeInfo?.let { route ->
        AppLogger.e("Google Maps", "Draw Polyline" + route.routePoints)
        val points = route.routePoints.map {
            CLLocationCoordinate2DMake(it.lat, it.lng)
        }
        val path = GMSMutablePath().apply {
            points.forEach { point ->
                addCoordinate(point)
            }
        }

        GMSPolyline().apply {
            strokeColor = UIColor.blueColor
            strokeWidth = 5.0
            this.path = path
            map = mapView
        }
    }
    return CurrentMapState(targetMarkerGms, markersGms, polyline)
}

data class CurrentMapState @OptIn(ExperimentalForeignApi::class) constructor(
    val targetMarkerGms: GMSMarker? = null,
    val markersGms: List<GMSMarker> = listOf(),
    val route: GMSPolyline? = null
)