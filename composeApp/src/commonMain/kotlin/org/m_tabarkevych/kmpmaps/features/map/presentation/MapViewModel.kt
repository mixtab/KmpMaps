package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.ext.getFullName
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.core.domain.client.LocationClient
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.SearchResult
import org.m_tabarkevych.kmpmaps.features.map.presentation.delegate.BuildRouteDelegate
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.BookmarkMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.DeleteMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkersUseCase
import org.m_tabarkevych.kmpmaps.features.menu.domain.manager.PlacesManager
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetThemeUseCase
import org.m_tabarkevych.kmpmaps.features.weather.domain.usecase.GetWeatherDataUseCase

class MapViewModel(
    getThemeUseCase: GetThemeUseCase,
    getMarkersUseCase: GetMarkersUseCase,
    private val locationClient: LocationClient,
    private val bookMarkMarkerUseCase: BookmarkMarkerUseCase,
    private val deleteMarkerUseCase: DeleteMarkerUseCase,
    private val buildRouteDelegate: BuildRouteDelegate,
    private val getWeatherDataUseCase: GetWeatherDataUseCase,
    private val placesManager: PlacesManager
) : MVIViewModel<MapUiState, MapUiEvent, MapUiEffect>() {

    private val routeInfoState = buildRouteDelegate.routeInfoState
        .onEach { if (it != null) _mapAction.emit(MapAction.ZoomToBounds(it.routePoints)) }

    private val _uiState = MutableStateFlow(MapUiState())
    override val uiState = combine(
        _uiState,
        getThemeUseCase.invoke(),
        getMarkersUseCase.invoke(),
        routeInfoState
    ) { uiState, theme, markers, routeInfo ->
        uiState.copy(theme = theme, markers = markers, routeInfo = routeInfo)
    }
        .onEach { AppLogger.i("ScreenUiState", it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MapUiState())

    private val _mapAction = MutableSharedFlow<MapAction>(1)
    val mapAction = _mapAction.asSharedFlow()


    override fun processUiEvent(event: MapUiEvent) {
        when (event) {
            MapUiEvent.OnSettingsClicked -> setUiEffect { MapUiEffect.NavigateToSettings }
            MapUiEvent.OnMarkersTitleClicked -> setUiEffect { MapUiEffect.NavigateToMarkers }
            MapUiEvent.OnMenuClicked -> showMenu()
            MapUiEvent.OnMenuClosed -> _uiState.update { it.copy(menuVisible = false) }
            MapUiEvent.OnMapLayerClicked -> updateMapLayer()
            MapUiEvent.OnMapClicked -> _uiState.update { it.copy(followUser = false) }
            is MapUiEvent.OnMapLongLicked -> handleMapLongClicked(event.location)
            is MapUiEvent.OnMarkerClicked -> handleMarkerClicked(event.markerUi)
            MapUiEvent.OnRecenterClicked -> zoomToUserLocation()
            is MapUiEvent.OnSearchValueChanged -> handleSearchValue(event.value)
            is MapUiEvent.OnSearchResultClicked -> handleSearchResultClicked(event.item)
            is MapUiEvent.OnBottomSheetValueChange -> handleBottomSheetStateChanged(event.isFoolExpanded)
            MapUiEvent.OnBottomSheetCloseClicked -> backToDefaultState()
            MapUiEvent.OnBookmarkMarkerClicked -> handleBookmarkButtonClicked()
            MapUiEvent.OnRemoveMarkerClicked -> handleRemoveMarkerClicked()
            MapUiEvent.OnDirectionsClicked -> calculateRoute()
            MapUiEvent.OnRouteDismissClicked -> {
                backToDefaultState()
                buildRouteDelegate.clearRoute()
            }
        }
    }

    private fun showMenu() {
        _uiState.update { it.copy(menuVisible = true) }
        setUiEffect { MapUiEffect.ShowMenu }
    }

    private fun backToDefaultState() {
        _uiState.update {
            it.copy(
                currentMarker = null,
                bottomSheetFoolExpanded = false,
                searchResults = listOf(),
                searchValue = ""
            )
        }
    }

    fun onPermissionGranted() {
        viewModelScope.launch {
            _uiState.update { it.copy(locationPermissionGranted = true) }

            locationClient.getLocationUpdates().collectLatest { location ->
                if (currentState.weatherData == null) {
                    val weatherData = getWeatherDataUseCase.invoke(location.lat, location.lng)

                    weatherData.onSuccess {
                        AppLogger.i(MapViewModel::class.getFullName(), "Weather data: $it")
                        _uiState.update { state -> state.copy(weatherData = it) }
                    }
                }

                AppLogger.i(MapViewModel::class.getFullName(), "new user location received")
                _uiState.update { it.copy(userLocation = location) }
                if (currentState.followUser)
                    _mapAction.emit(MapAction.ZoomToLocation(location))
            }
        }
    }

    private fun updateMapLayer() {
        _uiState.update { it.copy(isSatelliteView = !it.isSatelliteView) }
    }

    private fun handleMapLongClicked(coordinates: Coordinates, title: String? = null) {
        viewModelScope.launch {

            placesManager.fetchInfoForCoordinates(
                Coordinates(coordinates.lat, coordinates.lng)
            ).firstOrNull()?.onSuccess { item ->
                _uiState.update { state ->
                    state.copy(
                        currentMarker = MarkerUi.createMarker(
                            latitude = coordinates.lat,
                            longitude = coordinates.lng,
                            title = title ?: item.title,
                            address = item.description,
                            comment = "",
                        )
                    )
                }
                zoomToCoordinates(coordinates)
                setUiEffect { MapUiEffect.ExpandBottomSheet }
            }
        }
    }

    private fun handleMarkerClicked(markerUi: MarkerUi) {
        _uiState.update { state ->
            state.copy(currentMarker = markerUi, bottomSheetFoolExpanded = true)
        }
        setUiEffect { MapUiEffect.ExpandBottomSheet }
        zoomToCoordinates(Coordinates(markerUi.latitude, markerUi.longitude))
    }

    private fun zoomToUserLocation() {
        val location = currentState.userLocation ?: return
        _mapAction.tryEmit(MapAction.ZoomToLocation(location))
        _uiState.update { it.copy(followUser = true) }
    }

    private fun zoomToCoordinates(coordinates: Coordinates) {
        _mapAction.tryEmit(MapAction.ZoomToLocation(coordinates))
        _uiState.update { it.copy(followUser = false) }
    }

    private fun handleSearchValue(value: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(searchValue = value) }
            if (value.isNotEmpty()) {
                placesManager.fetchPlacesByText(
                    value,
                    Coordinates(
                        currentState.userLocation?.lat ?: 0.0,
                        currentState.userLocation?.lng ?: 0.0
                    ),
                ).collectLatest {
                    it.onSuccess { items ->
                        _uiState.update { state -> state.copy(searchResults = items) }
                    }
                }
            } else {
                _uiState.update { state -> state.copy(searchResults = listOf()) }
            }
        }
    }

    private fun handleSearchResultClicked(item: SearchResult) {
        viewModelScope.launch {
            if (item.coordinates != null) {
                handleMapLongClicked(item.coordinates, item.title)
            } else {
                placesManager.fetchPlaceInfoById(item.id).collectLatest { result ->
                    result.onSuccess { coordinates ->
                        handleMapLongClicked(coordinates, item.title)
                    }
                }
            }
        }
    }

    private fun handleBottomSheetStateChanged(isFoolExpanded: Boolean) {
        when {
            isFoolExpanded ->
                _uiState.update { it.copy(bottomSheetFoolExpanded = isFoolExpanded) }

            uiState.value.currentMarker != null -> {
                _uiState.update {
                    it.copy(
                        bottomSheetFoolExpanded = true, searchResults = listOf(), searchValue = ""
                    )
                }
            }

            else -> backToDefaultState()

        }
    }

    private fun handleBookmarkButtonClicked() {
        val marker = currentState.currentMarker ?: return
        viewModelScope.launch {
            bookMarkMarkerUseCase.invoke(marker)
            setUiEffect { MapUiEffect.NavigateToEditMarker(marker.id) }
        }
    }

    private fun handleRemoveMarkerClicked() {
        viewModelScope.launch {
            deleteMarkerUseCase.invoke(currentState.currentMarker?.id ?: return@launch)
                .onSuccess { backToDefaultState() }
        }
    }

    private fun calculateRoute() {
        viewModelScope.launch {
            buildRouteDelegate.calculateRoute(
                currentState.userLocation ?: return@launch,
                Coordinates(
                    currentState.currentMarker?.latitude ?: return@launch,
                    currentState.currentMarker?.longitude ?: return@launch
                )
            )
        }
    }
}