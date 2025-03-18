package org.m_tabarkevych.kmpmaps.features.menu.domain.manager

import cocoapods.GooglePlaces.GMSPlace
import cocoapods.GooglePlaces.GMSPlaceCircularLocationOption
import cocoapods.GooglePlaces.GMSPlacePropertyCoordinate
import cocoapods.GooglePlaces.GMSPlacePropertyFormattedAddress
import cocoapods.GooglePlaces.GMSPlacePropertyName
import cocoapods.GooglePlaces.GMSPlacePropertyPlaceID
import cocoapods.GooglePlaces.GMSPlaceSearchByTextRankPreference
import cocoapods.GooglePlaces.GMSPlaceSearchByTextRequest
import cocoapods.GooglePlaces.GMSPlaceSearchByTextResultCallback
import cocoapods.GooglePlaces.GMSPlacesClient
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.domain.toSearchResult
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLPlacemark
import platform.MapKit.MKDirections
import platform.MapKit.MKDirectionsRequest
import platform.MapKit.MKLocalSearch
import platform.MapKit.MKLocalSearchRequest
import platform.MapKit.MKMapItem
import platform.MapKit.MKPlacemark
import platform.MapKit.MKRoute

actual class PlacesManager actual constructor() {


    @OptIn(ExperimentalForeignApi::class)
    actual fun calculateRoute(
        startPosition: Coordinates,
        endPosition: Coordinates
    ): Flow<DomainResult<List<RouteInfo>>> = callbackFlow {
        val request = MKDirectionsRequest().apply {
            source = MKMapItem(
                MKPlacemark(
                    coordinate = CLLocationCoordinate2DMake(startPosition.lat, startPosition.lng),
                    addressDictionary = null
                )
            )
            destination = MKMapItem(
                MKPlacemark(
                    coordinate = CLLocationCoordinate2DMake(endPosition.lat, endPosition.lng),
                    addressDictionary = null
                )
            )
            // transportType = MKDirectionsTransportTyp
        }

        val directions = MKDirections(request)
        AppLogger.i("Places Manager", "calculateDirectionsWithCompletionHandler")
        directions.calculateDirectionsWithCompletionHandler { response, error ->
            if (error != null || response == null) {
                trySend(DomainResult.Failure(DataError.UNKNOWN))
                return@calculateDirectionsWithCompletionHandler
            }

            val routes = (response.routes as? List<MKRoute>)?.map { route ->
                AppLogger.i("Places Manager", "On Response:$route")
                val coordinates = mutableListOf<Coordinates>()
                val polyline = route.polyline
                val pointCount = polyline.pointCount.toInt()

                val routePoints = polyline.points()
                repeat(pointCount) { index ->
                    val location = routePoints?.get(index)
                    if (location != null) {
                        coordinates.add(Coordinates(location.x, location.y))
                    }
                }

                RouteInfo(
                    durationInSeconds = route.expectedTravelTime.toLong(),
                    distanceInMeters = route.distance.toLong(),
                    routePoints = coordinates
                )
            } ?: emptyList()

            AppLogger.i("Places Manager", "try send:" + DomainResult.Success(routes))
            trySend(DomainResult.Success(routes))
        }

        awaitClose { }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun fetchPlacesByText(
        searchValue: String,
        userPosition: Coordinates,
    ): Flow<DomainResult<List<SearchResult>>> = callbackFlow {
        val myProperties = listOf(GMSPlacePropertyName, GMSPlacePropertyPlaceID,
            GMSPlacePropertyCoordinate, GMSPlacePropertyFormattedAddress)

        val request = GMSPlaceSearchByTextRequest(searchValue, myProperties).apply {
            maxResultCount = 5
            isStrictTypeFiltering = true
            locationBias = GMSPlaceCircularLocationOption(
                CLLocationCoordinate2DMake(userPosition.lat, userPosition.lng),
                20000.0
            )
        }

        val callback: GMSPlaceSearchByTextResultCallback = { results, error ->
            if (error != null) {
                println(error.localizedDescription)
            }
            val results = results as? List<GMSPlace> ?: emptyList()

            if (results.isNotEmpty()) {
                trySendBlocking(DomainResult.Success(results.toSearchResult(userPosition)))
            }
        }

        GMSPlacesClient.sharedClient().searchByTextWithRequest(request, callback)

        awaitClose()
    }

    actual fun fetchInfoForCoordinates(
        coordinates: Coordinates
    ): Flow<DomainResult<SearchResult>> = callbackFlow {
        val geocoder = CLGeocoder()
        val location = CLLocation(coordinates.lat, coordinates.lng)


        geocoder.reverseGeocodeLocation(location) { placemarks, error ->
            if (error != null) {
                trySend(DomainResult.Failure(DataError.UNKNOWN))
                close()
                return@reverseGeocodeLocation
            }

            val placemark = placemarks?.firstOrNull() as? CLPlacemark
            if (placemark != null) {
                val addressLine = placemark.name ?: "Unknown Address"
                val city = placemark.locality
                val country = placemark.country

                trySend(DomainResult.Success(SearchResult("-1", addressLine, city ?: "", 0)))
            } else {
                trySend(DomainResult.Failure(DataError.UNKNOWN))
            }
            close()
        }

        awaitClose {
            geocoder.cancelGeocode()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun fetchPlaceInfoById(placeId: String): Flow<DomainResult<Coordinates>> =
        callbackFlow {
            val request = MKLocalSearchRequest().apply {
                naturalLanguageQuery = placeId
            }

            val search = MKLocalSearch(request)
            search.startWithCompletionHandler { response, error ->
                if (error != null || response == null || response.mapItems.isEmpty()) {
                    trySend(DomainResult.Failure(DataError.UNKNOWN))
                    return@startWithCompletionHandler
                }

                val mapItem = response.mapItems.firstOrNull() as? MKMapItem
                mapItem?.placemark?.location?.coordinate?.useContents {
                    trySend(
                        DomainResult.Success(Coordinates(this.latitude, this.longitude))
                    )

                } ?: trySend(DomainResult.Failure(DataError.UNKNOWN))

            }

            awaitClose { search.cancel() }
        }


}