package org.m_tabarkevych.kmpmaps.features.core.domain.client

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.m_tabarkevych.kmpmaps.KmpMapsApplication
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates

actual class LocationClient actual constructor() {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(KmpMapsApplication.instance)
    }

    // Implement the getCurrentLocation() method
    @SuppressLint("MissingPermission")
    actual fun getLocationUpdates(): Flow<Coordinates> {
        return callbackFlow {
            val locationManager =
                KmpMapsApplication.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnable =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (isGpsEnable.not() && isNetworkEnable.not()) {
                throw Exception("GPS is disabled")
            }
            val locationRequest = LocationRequest.create()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    val lastLocation = result.locations.lastOrNull()
                    lastLocation?.let { trySendBlocking(Coordinates(it.latitude, it.longitude)) }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

}