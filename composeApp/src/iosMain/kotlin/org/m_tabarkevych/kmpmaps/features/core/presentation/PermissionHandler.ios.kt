package org.m_tabarkevych.kmpmaps.features.core.presentation

import androidx.compose.runtime.Composable
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

class LocationPermissionDelegate(private val onResult: (Boolean) -> Unit) : NSObject(),
    CLLocationManagerDelegateProtocol {
    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        val granted = didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedAlways ||
                didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse
        onResult(granted)
    }
}

@Composable
actual fun RequestLocationPermission(onResult: (Boolean) -> Unit) {
    val locationManager = CLLocationManager()
    val delegate = LocationPermissionDelegate(onResult)

    locationManager.delegate = delegate

    when (CLLocationManager.authorizationStatus()) {
        kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
            onResult(true)
        }

        kCLAuthorizationStatusDenied, kCLAuthorizationStatusRestricted -> {
            onResult(false)
        }

        kCLAuthorizationStatusNotDetermined -> {
            locationManager.requestWhenInUseAuthorization()
            onResult(false) // Callback will be triggered later when the user grants/denies permission.
        }

        else -> onResult(false)
    }
}