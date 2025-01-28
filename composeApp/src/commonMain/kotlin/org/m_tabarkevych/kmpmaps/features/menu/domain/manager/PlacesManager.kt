package org.m_tabarkevych.kmpmaps.features.menu.domain.manager

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult

expect class PlacesManager() {

     fun calculateRoute(
        startPosition: Coordinates,
        endPosition: Coordinates,
    ): Flow<DomainResult<List<RouteInfo>, DataError.Remote>>

      fun fetchPlacesByText(
        searchValue: String,
        userPosition: Coordinates,
    ): Flow<DomainResult<List<SearchResult>, DataError.Remote>>

    fun fetchInfoForCoordinates(
        coordinates: Coordinates,
    ): Flow<DomainResult<SearchResult, DataError.Remote>>

    fun fetchPlaceInfoById(placeId: String): Flow<DomainResult<Coordinates, DataError.Remote>>


}