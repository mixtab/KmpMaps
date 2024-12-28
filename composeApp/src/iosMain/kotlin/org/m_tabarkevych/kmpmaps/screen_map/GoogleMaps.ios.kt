package org.m_tabarkevych.kmpmaps.screen_map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSCameraUpdate
import cocoapods.GoogleMaps.GMSMapStyle
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSServices
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMaps() {
    val mapView = remember { GMSMapView() }
    val cameraPosition = GMSCameraPosition.cameraWithLatitude(
        latitude = 1.35,
        longitude = 103.87,
        zoom = 14.0f
    )
    val cameraUpdate = GMSCameraUpdate.setCamera(cameraPosition)
    mapView.moveCamera(cameraUpdate)

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView }
    )
}

