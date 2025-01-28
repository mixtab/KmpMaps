package org.m_tabarkevych.kmpmaps.features.map.presentation.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.compose_multiplatform
import kmpmaps.composeapp.generated.resources.ic_plus
import kmpmaps.composeapp.generated.resources.ic_saved_marker
import kmpmaps.composeapp.generated.resources.markers_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.features.core.presentation.Grey10
import org.m_tabarkevych.kmpmaps.features.core.presentation.Grey99
import org.m_tabarkevych.kmpmaps.features.core.presentation.Zumthor
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.components.NoMarkersPlaceHolder

@Composable
fun MenuModalDrawerContent(
    markers: List<MarkerUi>,
    onMarkersTitleClicked: () -> Unit,
    onMarkerClicked: (MarkerUi) -> Unit,
    onSettingsClicked: () -> Unit,
    onCreateRouteClicked: () -> Unit
) {
    ModalDrawerSheet(drawerShape = RectangleShape) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopEnd)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        onSettingsClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
                Row(modifier = Modifier.padding(16.dp).align(Alignment.Start)) {
                    Image(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(Res.drawable.compose_multiplatform),
                        contentDescription = "User Image"
                    )
                    Column(Modifier.padding(start = 16.dp)) {
                        Text(
                            modifier = Modifier,
                            style = MaterialTheme.typography.titleLarge,
                            text = "Mykhailo Tabarkevych"
                        )
                        Text(
                            modifier = Modifier,
                            style = MaterialTheme.typography.titleMedium,
                            text = "mykhailo.t@test.com"
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 16.dp).padding(vertical = 10.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onMarkersTitleClicked.invoke()
                            },
                        text =  stringResource(Res.string.markers_title),
                        color = Grey10,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        tint = Grey10,
                        contentDescription = "Navigate to Markers screen"
                    )
                }
                if (markers.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                        items(markers) { item ->
                            Column {
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        onMarkerClicked.invoke(item)
                                    }
                                ) {
                                    Image(
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                            .size(20.dp),
                                        painter = painterResource(Res.drawable.ic_saved_marker),
                                        contentDescription = "Marker"
                                    )
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Text(text = item.title)
                                        Text(text = item.address, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                } else {
                    NoMarkersPlaceHolder()
                }
            }
            KmpButton(
                modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter),
                icon = Res.drawable.ic_plus,
                text = "Create route",
                onClick = {
                    onCreateRouteClicked.invoke()
                }
            )
        }
    }
}