package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_locate_off
import kmpmaps.composeapp.generated.resources.ic_locate_on
import kmpmaps.composeapp.generated.resources.ic_map_layer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.app.AppLogger
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.RequestLocationPermission
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.GoogleMaps
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.MenuModalDrawerContent
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.WeatherPanel
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet.MapDefaultBottomSheetContent
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet.PoiInfoBottomSheetContent
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet.RouteInfoBottomSheetContent
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenRoute(viewModel: MapViewModel, navigate: (NavRoute) -> Unit) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val effect = viewModel.effect


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val focusManager = LocalFocusManager.current

    val bottomSheetState = rememberStandardBottomSheetState(
        confirmValueChange = { newValue ->
            val isFoolExpanded = newValue == SheetValue.Expanded
            if (isFoolExpanded.not()) focusManager.clearFocus()

            viewModel.sendUiEvent(
                MapUiEvent.OnBottomSheetValueChange(isFoolExpanded = isFoolExpanded)
            )
            true
        },
    )

    MapUiEffectHandler(effect, bottomSheetState, drawerState, navigate)
    MapScreen(
        uiState = state.value,
        mapAction = viewModel.mapAction,
        drawerState = drawerState,
        bottomSheetState = bottomSheetState,
        processUiEvent = {
            viewModel.sendUiEvent(it)
        }
    )

    RequestLocationPermission { granted ->
        AppLogger.i("Permission", "Permission granted: $granted")
        if (granted) viewModel.onPermissionGranted()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    uiState: MapUiState,
    mapAction: SharedFlow<MapAction>,
    drawerState: DrawerState,
    bottomSheetState: SheetState,
    processUiEvent: (MapUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    ModalNavigationDrawer(
        modifier = Modifier.navigationBarsPadding(),
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            MenuModalDrawerContent(
                markers = uiState.markers,
                onMarkersTitleClicked = {
                    scope.launch {
                        drawerState.close()
                        processUiEvent.invoke(MapUiEvent.OnMarkersTitleClicked)
                    }
                },
                onSettingsClicked = {
                    scope.launch {
                        drawerState.close()
                        processUiEvent.invoke(MapUiEvent.OnSettingsClicked)
                    }
                },
                onMarkerClicked = {
                    scope.launch {
                        drawerState.close()
                        processUiEvent.invoke(MapUiEvent.OnMarkerClicked(it))
                    }
                },
                onCreateRouteClicked = {
                    scope.launch {
                        drawerState.close()
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }
                }
            )
        },
    ) {
        val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

        Box {
            BottomSheetScaffold(
                sheetContent = {
                    AnimatedContent(
                        targetState = uiState.bottomSheetType,
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) { state ->
                        when (state) {
                            MapBottomSheetType.DEFAULT ->
                                MapDefaultBottomSheetContent(
                                    uiState.searchValue,
                                    focusRequester,
                                    scope,
                                    bottomSheetState,
                                    uiState.searchResults,
                                    onSearchValueChanged = {
                                        processUiEvent.invoke(MapUiEvent.OnSearchValueChanged(it))
                                    },
                                    onSearchResultClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnSearchResultClicked(it))
                                    }
                                )

                            MapBottomSheetType.POI_INFO ->
                                PoiInfoBottomSheetContent(
                                    showLoading = uiState.showRouteCalculationLoading,
                                    currentMarker =
                                        uiState.currentMarker ?: return@AnimatedContent,
                                    onDirectionsClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnDirectionsClicked)
                                    },
                                    onBookmarkMarkerClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnBookmarkMarkerClicked)
                                    },
                                    onRemoveMarkerClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnRemoveMarkerClicked)
                                    },
                                    onCloseClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnBottomSheetCloseClicked)
                                    }
                                )


                            MapBottomSheetType.ROUTE_INFO ->
                                RouteInfoBottomSheetContent(
                                    uiState.routeInfo ?: return@AnimatedContent,
                                    onRouteDismissClicked = {
                                        processUiEvent.invoke(MapUiEvent.OnRouteDismissClicked)
                                    }
                                )
                        }
                    }
                },
                sheetPeekHeight = 160.dp,
                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                sheetSwipeEnabled = uiState.isBottomSheetGesturesEnabled,
                scaffoldState = scaffoldState,
            ) { innerPadding ->
                var boxWidth = 0
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .onSizeChanged {
                            if (it.width > boxWidth) {
                                boxWidth = it.width
                            }
                        }
                        .padding(bottom = (innerPadding.calculateBottomPadding().value * 0.8).dp)
                ) {
                    GoogleMaps(
                        uiState.isSatelliteView,
                        uiState.theme,
                        uiState.showUserLocation,
                        uiState.userLocation,
                        uiState.currentMarker,
                        uiState.markers,
                        uiState.routeInfo,
                        mapAction,
                        onMapClicked = {
                            processUiEvent.invoke(MapUiEvent.OnMapClicked)
                        },
                        onMapLongClicked = { location ->
                            processUiEvent.invoke(MapUiEvent.OnMapLongLicked(location))
                        },
                        onMarkerClicked = {
                            processUiEvent.invoke(MapUiEvent.OnMarkerClicked(it))
                        }
                    )
                    var leftPanelHeight by remember { mutableStateOf(0) }
                    var weatherPanelHeight by remember { mutableStateOf(0) }


                    FloatingActionButton(
                        modifier = Modifier.statusBarsPadding().padding(16.dp).size(48.dp),
                        containerColor = Color.White,
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            tint = Color.Black,
                            contentDescription = "Menu"
                        )
                    }


                    AnimatedVisibility(
                        modifier = Modifier
                            .onSizeChanged { weatherPanelHeight = it.height }
                            .align(Alignment.TopEnd)
                            .offset {
                                IntOffset(
                                    x = -(boxWidth * 0.75).toInt(),
                                    y = bottomSheetState.requireOffset()
                                        .roundToInt() - weatherPanelHeight,
                                )
                            },
                        visible = uiState.showWeather,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                    ) {
                        WeatherPanel(
                            modifier = Modifier.padding(16.dp),
                            weatherData = uiState.weatherData ?: return@AnimatedVisibility
                        )
                    }

                    AnimatedVisibility(
                        modifier = Modifier
                            .onSizeChanged { leftPanelHeight = it.height }
                            .align(Alignment.TopEnd)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = bottomSheetState.requireOffset()
                                        .roundToInt() - leftPanelHeight,
                                )
                            },
                        visible = uiState.bottomSheetFoolExpanded.not(),
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            FloatingActionButton(
                                modifier = Modifier.size(48.dp),
                                containerColor = Color.White,
                                onClick = {
                                    processUiEvent.invoke(MapUiEvent.OnMapLayerClicked)
                                },
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                            ) {
                                Crossfade(targetState = uiState.isSatelliteView) { isChecked ->
                                    Icon(
                                        painterResource(Res.drawable.ic_map_layer),
                                        tint = if (isChecked) Color.Blue else Color.Black,
                                        contentDescription = "Settings button"
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.width(48.dp),
                                color = MaterialTheme.colorScheme.surface,
                            )

                            FloatingActionButton(
                                modifier = Modifier.size(48.dp),
                                containerColor = Color.White,
                                onClick = {
                                    processUiEvent.invoke(MapUiEvent.OnRecenterClicked)
                                },
                                shape = RoundedCornerShape(
                                    bottomStart = 8.dp,
                                    bottomEnd = 8.dp
                                ),
                            ) {
                                Crossfade(targetState = uiState.followUser) { isChecked ->
                                    Icon(
                                        painter = painterResource(
                                            if (isChecked) Res.drawable.ic_locate_on else Res.drawable.ic_locate_off
                                        ),
                                        tint = if (isChecked) Color.Blue else Color.Black,
                                        contentDescription = "Follow user"
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MapScreenPreview() {
    MapScreen(
        MapUiState(),
        MutableSharedFlow(),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        bottomSheetState = rememberStandardBottomSheetState(),
        processUiEvent = {})
}