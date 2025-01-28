package org.m_tabarkevych.kmpmaps.features.menu.domain.manager

import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.getScopeId
import org.m_tabarkevych.kmpmaps.KmpMapsApplication
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import org.m_tabarkevych.kmpmaps.features.map.domain.model.mapper.toSearchResult
import java.util.Locale
import java.util.concurrent.TimeUnit

actual class PlacesManager {

    private val placesClient: PlacesClient by lazy {
        val context = KmpMapsApplication.instance
        Places.initialize(context, "AIzaSyAYDqJ1OWT4xqvlY1S2j-f_m3x9_OqmrTE")
        return@lazy Places.createClient(context)
    }

    actual fun calculateRoute(
        startPosition: Coordinates,
        endPosition: Coordinates,
    ): Flow<DomainResult<List<RouteInfo>, DataError.Remote>> = callbackFlow {
        val mGeoApiContext = GeoApiContext.Builder()
            .apiKey("AIzaSyAYDqJ1OWT4xqvlY1S2j-f_m3x9_OqmrTE")
            .connectTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_REQUEST_TIME_OUT, TimeUnit.SECONDS)
            .build()

        DirectionsApi.newRequest(mGeoApiContext)
            .mode(TravelMode.DRIVING)
            .origin(
                com.google.maps.model.LatLng(
                    startPosition.lat,
                    startPosition.lng
                )
            )
            .destination(
                com.google.maps.model.LatLng(
                    endPosition.lat,
                    endPosition.lng
                )
            ).setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {

                  val routes =  result.routes.map { route ->
                        val coordinates: MutableList<Coordinates> = mutableListOf()
                        route.overviewPolyline.decodePath().forEach {
                            coordinates.add(Coordinates(it.lat, it.lng))
                        }
                        RouteInfo(
                            result.routes.first().legs.first().duration.inSeconds,
                            result.routes.first().legs.first().distance.inMeters,
                            coordinates
                        )
                    }



                    trySendBlocking(DomainResult.Success(routes))

                }

                override fun onFailure(e: Throwable) {
                    trySendBlocking(
                        DomainResult.Error(DataError.Remote.UNKNOWN)
                    )
                }
            })

        awaitClose { }
    }

    actual fun fetchPlacesByText(
        searchValue: String,
        userPosition: Coordinates,
    ): Flow<DomainResult<List<SearchResult>, DataError.Remote>> = callbackFlow {


        val token = AutocompleteSessionToken.newInstance()

        val request =
            FindAutocompletePredictionsRequest.builder()
                .setOrigin(LatLng(userPosition.lat, userPosition.lng))
                .setSessionToken(token)
                .setQuery(searchValue)
                .build()
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                trySend(DomainResult.Success(response.autocompletePredictions.toSearchResult()))
            }.addOnFailureListener { exception: Exception? ->
                trySend(DomainResult.Error(DataError.Remote.UNKNOWN))
            }

        awaitClose()

    }

    actual fun fetchInfoForCoordinates(
        coordinates: Coordinates,
    ): Flow<DomainResult<SearchResult, DataError.Remote>> = callbackFlow {

        val geocoder = Geocoder(KmpMapsApplication.instance, Locale.getDefault())
        val addresses  = geocoder.getFromLocation(coordinates.lat, coordinates.lng, 1)

        if (!addresses.isNullOrEmpty()) {
            val address: Address = addresses.firstOrNull()?:return@callbackFlow


            val addressLine = address.getAddressLine(0) // Повна адреса
            trySend(DomainResult.Success(SearchResult("-1", address.featureName?:"Marked place", addressLine, 0)))
        }

        awaitClose()

    }

    actual fun fetchPlaceInfoById(placeId: String): Flow<DomainResult<Coordinates, DataError.Remote>> =
        callbackFlow {
            val request =
                FetchPlaceRequest.newInstance(
                    placeId,
                    listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
                )

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val latLng = response.place.latLng
                    if (latLng == null) {
                        trySend(DomainResult.Error(DataError.Remote.UNKNOWN))
                    } else {
                        trySend(
                            DomainResult.Success(
                                Coordinates(
                                    latLng.latitude,
                                    latLng.longitude
                                )
                            )
                        )
                    }
                }.addOnFailureListener {
                    trySend(DomainResult.Error(DataError.Remote.UNKNOWN))
                }

            awaitClose()
        }

    companion object {
        const val DEFAULT_REQUEST_TIME_OUT = 30L
    }
}