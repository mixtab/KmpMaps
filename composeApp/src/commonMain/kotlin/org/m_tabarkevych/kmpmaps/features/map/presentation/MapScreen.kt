package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_bookmark_filled
import kmpmaps.composeapp.generated.resources.ic_bookmark_outlined
import kmpmaps.composeapp.generated.resources.ic_locate_off
import kmpmaps.composeapp.generated.resources.ic_locate_on
import kmpmaps.composeapp.generated.resources.ic_map_layer
import kmpmaps.composeapp.generated.resources.ic_marker
import kmpmaps.composeapp.generated.resources.ic_share
import kmpmaps.composeapp.generated.resources.remove_marker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.InitPermissionController
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.getScreenHeight
import org.m_tabarkevych.kmpmaps.features.core.presentation.launchUrl
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.GoogleMaps
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.MenuModalDrawerContent
import org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet.MapDefaultBottomSheetContent
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenRoute(viewModel: MapViewModel, navigate: (NavRoute) -> Unit) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val effect = viewModel.effect.collectAsStateWithLifecycle(null)


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val focusManager = LocalFocusManager.current

    val bottomSheetState = rememberStandardBottomSheetState(
        confirmValueChange = { newValue ->
            val isFoolExpanded = newValue == SheetValue.Expanded
            if (isFoolExpanded.not()) focusManager.clearFocus()

            viewModel.sendUiEvent(
                MapUiEvent.OnBottomSheetValueChange(
                    isFoolExpanded = isFoolExpanded
                )
            )
            true
        },
    )

    MapUiEffectHandler(effect.value, bottomSheetState, drawerState, navigate)
    MapScreen(
        uiState = state.value,
        mapAction = viewModel.mapAction,
        drawerState = drawerState,
        bottomSheetState = bottomSheetState,
        processUiEvent = {
            viewModel.sendUiEvent(it)
        }
    )


    val controller = InitPermissionController()
    LaunchedEffect(Unit) {
        viewModel.askLocationPermission(controller)
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
                    processUiEvent.invoke(MapUiEvent.OnMarkersTitleClicked)
                },
                onSettingsClicked = {
                    processUiEvent.invoke(MapUiEvent.OnSettingsClicked)
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

                    AnimatedVisibility(
                        uiState.bottomSheetType == MapBottomSheetType.DEFAULT,
                        enter = fadeIn(),
                        exit = fadeOut(tween(0))
                    ) {
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
                    }

                    AnimatedVisibility(
                        uiState.bottomSheetType == MapBottomSheetType.ROUTE_INFO,
                        enter = fadeIn(),
                        exit = fadeOut(tween(0))
                    ) {
                        val routeInfo = uiState.routeInfo ?: return@AnimatedVisibility
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.align(Alignment.Start)) {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = routeInfo.durationInSeconds.seconds.toString(),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = routeInfo.distanceInMeters.toString() + "m",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier.padding(end = 16.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                KmpButton(
                                    modifier = Modifier.weight(1f).padding(16.dp),
                                    "Start Navigation"
                                ) {
                                    launchUrl(routeInfo.googleUrl)
                                }
                                Text(
                                    modifier = Modifier.padding(16.dp)
                                        .align(Alignment.CenterVertically).clickable {
                                            processUiEvent.invoke(MapUiEvent.OnRouteDismissClicked)
                                        },
                                    text = "Dismiss",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        uiState.bottomSheetType == MapBottomSheetType.POI_INFO,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        val clipboardManager = LocalClipboardManager.current
                        if (uiState.currentMarker == null) return@AnimatedVisibility
                        Column(
                            Modifier.height((getScreenHeight().value * 0.4).dp)
                                .padding(horizontal = 16.dp)
                        ) {
                            Row {
                                Text(
                                    modifier = Modifier.weight(1f)
                                        .align(Alignment.CenterVertically),
                                    text = uiState.currentMarker.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                IconButton(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    onClick = {
                                        processUiEvent.invoke(MapUiEvent.OnBottomSheetCloseClicked)
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = uiState.currentMarker.address)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(Modifier.fillMaxWidth()) {
                                KmpButton(
                                    modifier = Modifier.weight(1f),
                                    text = "Directions",
                                    onClick = {
                                        processUiEvent.invoke(MapUiEvent.OnDirectionsClicked)
                                    }
                                )
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(
                                            annotatedString = buildAnnotatedString {
                                                append(text = uiState.currentMarker.googleUrl)
                                            }
                                        )
                                    }) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        painter = painterResource(Res.drawable.ic_share),
                                        contentDescription = "Share",
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        processUiEvent.invoke(MapUiEvent.OnBookmarkMarkerClicked)
                                    },
                                ) {
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(
                                            if (uiState.currentMarker.isBookMarked)
                                                Res.drawable.ic_bookmark_filled
                                            else
                                                Res.drawable.ic_bookmark_outlined
                                        ),
                                        contentDescription = "Bookmark",
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row {
                                Image(
                                    painter = painterResource(Res.drawable.ic_marker),
                                    contentDescription = "Marker",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = uiState.currentMarker.latitude.toString() + ", " + uiState.currentMarker.longitude
                                )
                            }
                            if (uiState.currentMarker.isBookMarked) {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        processUiEvent.invoke(MapUiEvent.OnRemoveMarkerClicked)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, Color.Red),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Text(
                                        stringResource(Res.string.remove_marker),
                                        color = Color.Red
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))

                        }
                    }
                },
                sheetPeekHeight = 160.dp,
                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                scaffoldState = scaffoldState,
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                    var composableHeight by remember { mutableStateOf(0) }


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
                            .onSizeChanged {
                                composableHeight = it.height
                            }
                            .align(Alignment.TopEnd)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = bottomSheetState.requireOffset()
                                        .roundToInt() - composableHeight,
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