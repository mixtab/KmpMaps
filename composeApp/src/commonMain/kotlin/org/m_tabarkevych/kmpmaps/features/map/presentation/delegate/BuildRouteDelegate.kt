package org.m_tabarkevych.kmpmaps.features.map.presentation.delegate

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.core.domain.client.LocationClient
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.menu.domain.manager.PlacesManager

class BuildRouteDelegate(
    private val locationClient: LocationClient,
    private val placesManager:PlacesManager
) {

    private val _routeInfoState = MutableStateFlow<RouteInfo?>(null)
    val routeInfoState = _routeInfoState.asStateFlow()

    suspend fun calculateRoute(startCoordinates: Coordinates, endCoordinates: Coordinates) {
        placesManager.calculateRoute(
            startCoordinates,
            endCoordinates
        ).collectLatest {
            it.onSuccess { routes ->
                _routeInfoState.update { routes.first() }
            }
        }
    }

    suspend fun calculateRouteFromUserPosition(endCoordinates: Coordinates) {
        placesManager.calculateRoute(
            locationClient.getLocationUpdates().firstOrNull() ?: return,
            endCoordinates
        ).collectLatest {
            it.onSuccess { routes ->
                AppLogger.i("BuildRouteDelegate", "onSuccess")
                _routeInfoState.update { routes.firstOrNull() }
            }
        }
    }

    fun clearRoute() = _routeInfoState.update { null }

}