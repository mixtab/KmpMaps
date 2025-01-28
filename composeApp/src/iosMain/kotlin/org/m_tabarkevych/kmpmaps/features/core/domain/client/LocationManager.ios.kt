package org.m_tabarkevych.kmpmaps.features.core.domain.client

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.ext.getFullName
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class LocationClient actual constructor() {

    private val locationManager: CLLocationManager by lazy { CLLocationManager() }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getLocationUpdates(): Flow<Coordinates> = callbackFlow {
        AppLogger.i(LocationClient::class.getFullName(), "Start location updates")
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                AppLogger.i(LocationClient::class.getFullName(), "On location received")
                val location = didUpdateLocations.firstOrNull() as? CLLocation?
                if (location != null) {
                    AppLogger.i(LocationClient::class.getFullName(), location.toString())
                    trySend(
                        location.coordinate.useContents {
                            Coordinates(
                                lat = latitude,
                                lng = longitude
                            )
                        }

                    ).isSuccess
                }
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                close(RuntimeException("Failed to fetch location: ${didFailWithError.localizedDescription}"))
            }

        }

        locationManager.delegate = delegate
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()

        awaitClose {
            locationManager.stopUpdatingLocation()
            locationManager.delegate = null
        }
    }
}
