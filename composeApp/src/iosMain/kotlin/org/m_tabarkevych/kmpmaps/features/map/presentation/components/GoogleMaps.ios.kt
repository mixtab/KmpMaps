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
import platform.UIKit.UIImage
import platform.darwin.NSObject

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
                    val userPosition = GMSCameraPosition.cameraWithLatitude(
                        latitude = action.location.lat,
                        longitude = action.location.lng,
                        zoom = mapView.camera.zoom
                    )
                    mapView.moveCamera(GMSCameraUpdate.setCamera(userPosition))
                }

                is MapAction.ZoomToBounds -> {
                    if (action.bounds.isNotEmpty()) {
                        AppLogger.i("Google maps", "ZoomToBounds")

                        val path = GMSMutablePath().apply {
                            action.bounds.forEach { coordinate ->
                                addCoordinate(
                                    CLLocationCoordinate2DMake(
                                        coordinate.lat,
                                        coordinate.lng
                                    )
                                )
                            }
                        }

                        val southwest = CLLocationCoordinate2DMake(
                            action.bounds.first().lat,
                            action.bounds.first().lng
                        )
                        val northeast = CLLocationCoordinate2DMake(
                            action.bounds.last().lat,
                            action.bounds.last().lat
                        )
                        val bounds = GMSCoordinateBounds(southwest, northeast)
                        val cameraUpdate = GMSCameraUpdate.fitBounds(bounds, 50.0) // padding 50px
                        mapView.animateWithCameraUpdate(cameraUpdate)
                    }
                }
            }
        }

    }

    currentMarker?.let {
        val marker = GMSMarker().apply {
            icon = UIImage.imageNamed("ic_marker")
            setPosition(CLLocationCoordinate2DMake(it.latitude, it.longitude))
        }
        marker.setMap(mapView)
    }
    markers.forEach { marker ->
        AppLogger.e("Google Maps", "Add Marker")
        val marker = GMSMarker().apply {
            icon = UIImage.imageNamed("ic_marker")
            setPosition(CLLocationCoordinate2DMake(marker.latitude, marker.longitude))
        }

        marker.setMap(mapView)
    }

    routeInfo?.let { info ->
        val points = info.routePoints.map {
            CLLocationCoordinate2DMake(it.lat, it.lng)
        }
        val path = GMSMutablePath().apply {
            points.forEach { point ->
                addCoordinate(point)
            }
        }

        AppLogger.e("Google Maps", "Add polyline")
        GMSPolyline().apply {
            this.path = path
            this.map = mapView
        }
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

        }
    }
    mapView.delegate = delegate

    AppLogger.e("Google Maps", "UIKitView")
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = { view ->
            view.settings.setAllGesturesEnabled(true)
            view.settings.setScrollGestures(true)
            view.settings.setZoomGestures(true)
            view.settings.setCompassButton(false)

            view.myLocationEnabled = true // show the users dot
            mapView.userInteractionEnabled = true
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        )
    )
}

@Composable
fun IosTest() {

}
