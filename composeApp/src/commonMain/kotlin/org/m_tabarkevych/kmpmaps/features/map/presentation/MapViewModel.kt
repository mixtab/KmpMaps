package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.ext.getFullName
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.features.core.domain.client.LocationClient
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.presentation.delegate.BuildRouteDelegate
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.BookmarkMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.DeleteMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkersUseCase
import org.m_tabarkevych.kmpmaps.features.menu.domain.manager.PlacesManager
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetThemeUseCase

class MapViewModel(
    getThemeUseCase: GetThemeUseCase,
    getMarkersUseCase: GetMarkersUseCase,
    private val locationClient: LocationClient,
    private val bookMarkMarkerUseCase: BookmarkMarkerUseCase,
    private val deleteMarkerUseCase: DeleteMarkerUseCase,
    private val buildRouteDelegate: BuildRouteDelegate,
    private val placesManager: PlacesManager
) : MVIViewModel<MapUiState, MapUiEvent, MapUiEffect>() {


    private val routeInfoState =  buildRouteDelegate.routeInfoState
        .onEach {
            if (it != null)
                _mapAction.emit(MapAction.ZoomToBounds(it.routePoints))
        }

    private val _uiState = MutableStateFlow(MapUiState())
    override val uiState = combine(
        _uiState,
        getThemeUseCase.invoke(),
        getMarkersUseCase.invoke(),
        routeInfoState
    ) { uiState, theme, markers, routeInfo ->
        uiState.copy(theme = theme, markers = markers, routeInfo = routeInfo)
    }
        .onEach {
            AppLogger.i("ScreenUiState", it.toString())
        }.onStart {
            onPermissionGranted()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MapUiState())

    private val _mapAction = MutableSharedFlow<MapAction>(1)
    val mapAction = _mapAction.asSharedFlow()


    override fun processUiEvent(event: MapUiEvent) {
        when (event) {
            MapUiEvent.OnSettingsClicked -> setUiEffect { MapUiEffect.NavigateToSettings }
            MapUiEvent.OnMarkersTitleClicked -> { setUiEffect { MapUiEffect.NavigateToMarkers } }
            MapUiEvent.OnCalculateRouteClicked -> {

            }
            MapUiEvent.OnMenuClicked -> {
                _uiState.update { it.copy(menuVisible = true) }
                setUiEffect { MapUiEffect.ShowMenu }
            }

            MapUiEvent.OnMenuClosed -> _uiState.update { it.copy(menuVisible = false) }

            MapUiEvent.OnMapLayerClicked -> updateMapLayer()
            MapUiEvent.OnMapClicked -> _uiState.update { it.copy(followUser = false) }
            is MapUiEvent.OnMapLongLicked -> handleMapLongClicked(event.location)
            is MapUiEvent.OnMarkerClicked -> {
                _uiState.update { state ->
                    state.copy(
                        currentMarker = event.markerUi,
                        bottomSheetFoolExpanded = true
                    )
                }
                setUiEffect { MapUiEffect.ExpandBottomSheet }
                zoomToCoordinates(Coordinates(event.markerUi.latitude, event.markerUi.longitude))
            }

            MapUiEvent.OnRecenterClicked -> zoomToUserLocation()

            is MapUiEvent.OnSearchValueChanged -> handleSearchValue(event.value)
            is MapUiEvent.OnSearchResultClicked -> {
                viewModelScope.launch {
                    placesManager.fetchPlaceInfoById(event.item.id).collectLatest { result ->
                        result.onSuccess { coordinates ->
                            handleMapLongClicked(coordinates, event.item.title)
                        }
                    }
                }
            }

            is MapUiEvent.OnBottomSheetValueChange -> {
                if (event.isFoolExpanded) {
                    _uiState.update {
                        it.copy(
                            bottomSheetFoolExpanded = event.isFoolExpanded,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            currentMarker = null,
                            bottomSheetFoolExpanded = false,
                            searchResults = listOf(),
                            searchValue = ""
                        )
                    }
                }
            }

            MapUiEvent.OnBottomSheetCloseClicked -> {
                setUiEffect { MapUiEffect.PartialExpandBottomSheet }
            }

            MapUiEvent.OnBookmarkMarkerClicked -> {
                val marker = currentState.currentMarker ?: return
                viewModelScope.launch {
                    bookMarkMarkerUseCase.invoke(marker)
                    setUiEffect { MapUiEffect.NavigateToEditMarker(marker.id) }
                }
            }

            MapUiEvent.OnRemoveMarkerClicked -> {
                viewModelScope.launch {
                    deleteMarkerUseCase.invoke(currentState.currentMarker?.id ?: return@launch)
                        .onSuccess {
                            setUiEffect { MapUiEffect.PartialExpandBottomSheet }
                        }
                }
            }

            MapUiEvent.OnDirectionsClicked -> {
                viewModelScope.launch {
                    buildRouteDelegate.calculateRoute(
                        currentState.userLocation ?: return@launch,
                        Coordinates(
                            currentState.currentMarker?.latitude ?: 0.0,
                            currentState.currentMarker?.longitude ?: 0.0
                        )
                    )
                }
            }
            MapUiEvent.OnRouteDismissClicked -> buildRouteDelegate.clearRoute()

        }
    }

    private var test: PermissionsController? = null
    fun askLocationPermission(permissionsController: PermissionsController) {
        viewModelScope.launch {
            try {
                test = permissionsController
                AppLogger.i(MapViewModel::class.getFullName(), "Ask location permission")
                permissionsController.providePermission(Permission.LOCATION)

            } catch (deniedAlways: DeniedAlwaysException) {
                _uiState.update { it.copy(locationPermissionGranted = false) }
                AppLogger.i(MapViewModel::class.getFullName(), "Location Permission Failed")
            } catch (denied: DeniedException) {
                _uiState.update { it.copy(locationPermissionGranted = false) }
                AppLogger.i(MapViewModel::class.getFullName(), "Location Permission Failed")
            }
        }
    }

    private fun onPermissionGranted() {
        viewModelScope.launch {
            if (test?.isPermissionGranted(Permission.LOCATION) == true) {
                _uiState.update { it.copy(locationPermissionGranted = true) }

                locationClient.getLocationUpdates().collectLatest { location ->
                    AppLogger.i(MapViewModel::class.getFullName(), "new user location received")
                    _uiState.update { it.copy(userLocation = location) }
                    _mapAction.emit(MapAction.ZoomToLocation(location))
                }
            }
        }
    }

    private fun updateMapLayer() {
        _uiState.update {
            it.copy(isSatelliteView = !it.isSatelliteView)
        }
    }

    private fun handleMapLongClicked(coordinates: Coordinates, title: String? = null) {
        viewModelScope.launch {

            placesManager.fetchInfoForCoordinates(
                Coordinates(
                    coordinates.lat,
                    coordinates.lng
                )
            ).collectLatest {
                it.onSuccess { item ->
                    _uiState.update { state ->
                        state.copy(
                            currentMarker = MarkerUi.createMarker(
                                latitude = coordinates.lat,
                                longitude = coordinates.lng,
                                title = title ?: item.title,
                                address = item.description,
                                comment = "",
                            ),
                            bottomSheetFoolExpanded = true
                        )
                    }
                    setUiEffect { MapUiEffect.ExpandBottomSheet }
                    zoomToCoordinates(coordinates)
                }
            }

        }

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
            _uiState.emit(
                currentState.copy(
                    searchValue = value
                )
            )
            if (value.isNotEmpty()) {
                placesManager.fetchPlacesByText(
                    value,
                    Coordinates(
                        currentState.userLocation?.lat ?: 0.0,
                        currentState.userLocation?.lng ?: 0.0
                    ),
                ).collectLatest {
                    it.onSuccess { items ->
                        _uiState.update { state ->
                            state.copy(searchResults = items)
                        }
                    }
                }
            } else {
                _uiState.update { state ->
                    state.copy(searchResults = listOf())
                }
            }
        }


    }
}