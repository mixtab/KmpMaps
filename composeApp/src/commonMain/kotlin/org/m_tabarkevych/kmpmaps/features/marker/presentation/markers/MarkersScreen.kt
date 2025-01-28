package org.m_tabarkevych.kmpmaps.features.marker.presentation.markers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_saved_marker
import kmpmaps.composeapp.generated.resources.markers_option_place_options
import kmpmaps.composeapp.generated.resources.markers_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.Toolbar
import org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.components.NoMarkersPlaceHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkersRoute(
    viewModel: MarkersViewModel, navigate: (NavRoute) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.bottomSheetVisible) {
        if (state.bottomSheetVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    MarkersEffectHandler(viewModel.effect, navigate)
    MarkersScreen(
        uiState = state,
        processUiEvent = { viewModel.sendUiEvent(it) },
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkersScreen(
    uiState: MarkersUiState,
    sheetState: SheetState,
    processUiEvent: (MarkersUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.bottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { processUiEvent.invoke(MarkersUiEvent.OnBottomSheetCloseClicked) },
                sheetState = sheetState
            ) {
                if (uiState.currentSelectedMarker == null) return@ModalBottomSheet
                Column {
                    Row(Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
                        Column(modifier = Modifier.align(Alignment.CenterVertically).weight(1f)) {
                            Text(
                                text = uiState.currentSelectedMarker.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                stringResource(Res.string.markers_option_place_options),
                                fontSize = 12.sp
                            )
                        }
                        IconButton(onClick = {
                            processUiEvent.invoke(MarkersUiEvent.OnBottomSheetCloseClicked)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentDescription = "Close options"
                            )
                        }

                    }
                    HorizontalDivider(Modifier.fillMaxWidth())
                    LazyColumn {
                        itemsIndexed(MarkerOption.entries) { index, item ->
                            Row(modifier = Modifier.clickable {
                                processUiEvent.invoke(
                                    MarkersUiEvent.OnMarkerOptionSelected(
                                        uiState.currentSelectedMarker,
                                        item
                                    )
                                )
                            }.padding(16.dp)) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(item.title),
                                    color = item.textColor
                                        ?: MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Toolbar(title = stringResource(Res.string.markers_title), onBackClick = {
                processUiEvent.invoke(MarkersUiEvent.OnBackClicked)
            })
            if (uiState.markers.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(uiState.markers) { marker ->
                        Column {
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    // onMarkerClicked.invoke(item)
                                }
                            ) {
                                Image(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                        .size(20.dp),
                                    painter = painterResource(Res.drawable.ic_saved_marker),
                                    contentDescription = "Marker"
                                )
                                Spacer(Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = marker.title,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = marker.address,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 12.sp
                                    )
                                }
                                IconButton(onClick = {
                                    processUiEvent.invoke(
                                        MarkersUiEvent.OnMarkerOptionsClicked(
                                            marker
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        contentDescription = "Marker Options"
                                    )
                                }

                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                            )
                        }
                    }
                }
            } else {
                NoMarkersPlaceHolder()
            }
        }
    }
}