package org.m_tabarkevych.kmpmaps.features.map.presentation

import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEffect
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEvent
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiState
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.WeatherData

data class MapUiState(
    val showLoading: Boolean = false,
    val menuVisible: Boolean = false,
    val locationPermissionGranted: Boolean = false,
    val userLocation: Coordinates? = null,
    val weatherData:WeatherData? = null,
    val followUser: Boolean = true,
    val isSatelliteView: Boolean = false,
    val theme: Theme = Theme.FOLLOW_SYSTEM,
    val searchValue: String = "",
    val searchResults: List<SearchResult> = listOf(),
    val currentMarker: MarkerUi? = null,
    val markers: List<MarkerUi> = listOf(),
    val bottomSheetFoolExpanded: Boolean = false,
    val routeInfo: RouteInfo? = null,
) : UiState {
    val showWeather = !bottomSheetFoolExpanded && weatherData != null
    val showUserLocation = locationPermissionGranted

    val bottomSheetType: MapBottomSheetType = when {
        routeInfo != null -> MapBottomSheetType.ROUTE_INFO
        currentMarker != null -> MapBottomSheetType.POI_INFO
        else -> MapBottomSheetType.DEFAULT
    }

    val isBottomSheetGesturesEnabled = bottomSheetType == MapBottomSheetType.DEFAULT
}


enum class MapBottomSheetType {
    DEFAULT, POI_INFO, ROUTE_INFO
}

sealed interface MapUiEvent : UiEvent {
    data object OnSettingsClicked : MapUiEvent
    data object OnMarkersTitleClicked : MapUiEvent
    data object OnMenuClicked : MapUiEvent
    data object OnMenuClosed : MapUiEvent
    data object OnRecenterClicked : MapUiEvent
    data object OnMapLayerClicked : MapUiEvent
    data object OnMapClicked : MapUiEvent
    data class OnMapLongLicked(val location: Coordinates) : MapUiEvent
    data class OnMarkerClicked(val markerUi: MarkerUi) : MapUiEvent
    data class OnBottomSheetValueChange(val isFoolExpanded: Boolean) : MapUiEvent
    data class OnSearchValueChanged(val value: String) : MapUiEvent
    data class OnSearchResultClicked(val item: SearchResult) : MapUiEvent
    data object OnBottomSheetCloseClicked : MapUiEvent
    data object OnBookmarkMarkerClicked : MapUiEvent
    data object OnRemoveMarkerClicked : MapUiEvent
    data object OnDirectionsClicked : MapUiEvent
    data object OnRouteDismissClicked : MapUiEvent
}

sealed interface MapUiEffect : UiEffect {
    data class ShowMessage(val message: String) : MapUiEffect
    data object ShowMenu : MapUiEffect
    data object HideMenu : MapUiEffect
    data object ExpandBottomSheet : MapUiEffect
    data object PartialExpandBottomSheet : MapUiEffect
    data object NavigateToMenu : MapUiEffect
    data object NavigateToSettings : MapUiEffect
    data object NavigateToMarkers : MapUiEffect
    data class NavigateToEditMarker(val markerId: String) : MapUiEffect
}

sealed class MapAction {
    data class ZoomToLocation(val location: Coordinates) : MapAction()
    data class ZoomToBounds(val bounds: List<Coordinates>) : MapAction()
}