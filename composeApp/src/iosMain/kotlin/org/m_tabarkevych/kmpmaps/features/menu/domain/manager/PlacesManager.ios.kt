package org.m_tabarkevych.kmpmaps.features.menu.domain.manager

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark

actual class PlacesManager actual constructor() {
    actual fun calculateRoute(
        startPosition: Coordinates,
        endPosition: Coordinates
    ): Flow<DomainResult<List<RouteInfo>, DataError.Remote>> {
        TODO("Not yet implemented")
    }

    actual fun fetchPlacesByText(
        searchValue: String,
        userPosition: Coordinates,
        searchTypes: List<String>,
        countries: List<String>
    ): Flow<DomainResult<List<SearchResult>, DataError.Remote>> {
        TODO("Not yet implemented")
    }

    actual fun fetchInfoForCoordinates(
        coordinates: Coordinates
    ): Flow<DomainResult<SearchResult, DataError.Remote>> = callbackFlow {
        val geocoder = CLGeocoder()
        val location = CLLocation(coordinates.lat, coordinates.lng)

        geocoder.reverseGeocodeLocation(location) { placemarks, error ->
            if (error != null) {
                trySend(DomainResult.Error(DataError.Remote.UNKNOWN))
                close()
                return@reverseGeocodeLocation
            }

            val placemark = placemarks?.firstOrNull() as? CLPlacemark
            if (placemark != null) {
                val addressLine = placemark.name ?: "Unknown Address"
                val city = placemark.locality
                val country = placemark.country

                trySend(DomainResult.Success(SearchResult("-1", addressLine, city?:"", 0)))
            } else {
                trySend(DomainResult.Error(DataError.Remote.UNKNOWN))
            }
            close()
        }

        awaitClose {
            geocoder.cancelGeocode()
        }
    }

    actual fun fetchPlaceInfoById(placeId: String): Flow<DomainResult<Coordinates, DataError.Remote>> {
        TODO("Not yet implemented")
    }


}