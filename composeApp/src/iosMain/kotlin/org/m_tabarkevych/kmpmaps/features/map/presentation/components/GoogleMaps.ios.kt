package org.m_tabarkevych.kmpmaps.features.map.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMapViewDelegateProtocol
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMarker.Companion.markerImageWithColor
import cocoapods.GoogleMaps.animateToCameraPosition
import cocoapods.GoogleMaps.kGMSTypeHybrid
import cocoapods.GoogleMaps.kGMSTypeNormal
import cocoapods.GoogleMaps.kGMSTypeSatellite
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
import platform.UIKit.UIColor
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMaps(
    isSatelliteView:Boolean,
    theme: Theme,
    isMyLocationEnabled: Boolean,
    userLocation: Coordinates?,
    currentMarker:MarkerUi?,
    markers:List<MarkerUi>,
    routeInfo: RouteInfo?,
    mapActionSharedFlow: SharedFlow<MapAction>,
    onMapClicked: () -> Unit,
    onMapLongClicked:(Coordinates) -> Unit,
    onMarkerClicked:(MarkerUi) -> Unit,
    ) {

    val mapView = remember { GMSMapView() }
    LaunchedEffect(Unit) {
        val cameraPosition = GMSCameraPosition.cameraWithLatitude(
            latitude = 50.450001,
            longitude = 30.523333,
            zoom = 13.0f
        )


       // val cameraUpdate = GMSCameraUpdate.setCamera(cameraPosition)
        mapView.animateToCameraPosition(cameraPosition)
    }

    LaunchedEffect(Unit) {
        mapActionSharedFlow.collectLatest {action ->
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

                is MapAction.ZoomToBounds -> TODO()
            }
        }

    }

    currentMarker?.let {
        val marker = GMSMarker().apply {
            markerImageWithColor(UIColor.blueColor())
            setPosition(CLLocationCoordinate2DMake(it.latitude,it.longitude))
        }
        marker.setMap(mapView)
    }

    markers.forEach { marker ->

        val marker = GMSMarker().apply {
            markerImageWithColor(UIColor.blueColor())
            setPosition(CLLocationCoordinate2DMake(marker.latitude,marker.longitude))
        }

        marker.setMap(mapView)
    }

    val type = if (isSatelliteView) kGMSTypeHybrid else kGMSTypeNormal
    mapView.mapType = type
    mapView.myLocationEnabled = isMyLocationEnabled


    val delegate = remember {
        object : NSObject(), GMSMapViewDelegateProtocol {
            override fun mapView(mapView: GMSMapView, willMove: Boolean) {
                if (willMove) onMapClicked.invoke()
            }

            override fun mapView(mapView: GMSMapView, didLongPressAtCoordinate: CValue<CLLocationCoordinate2D>) {
                didLongPressAtCoordinate.useContents {
                    onMapLongClicked.invoke(Coordinates(this.latitude,this.longitude))
                }
            }

        }
    }
    mapView.delegate = delegate

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView }
    )
}

