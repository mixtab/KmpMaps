package org.m_tabarkevych.kmpmaps.features.map.presentation.components.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.create_route
import kmpmaps.composeapp.generated.resources.directions
import kmpmaps.composeapp.generated.resources.ic_bookmark_filled
import kmpmaps.composeapp.generated.resources.ic_bookmark_outlined
import kmpmaps.composeapp.generated.resources.ic_marker
import kmpmaps.composeapp.generated.resources.ic_share
import kmpmaps.composeapp.generated.resources.remove_marker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.KmpButton
import org.m_tabarkevych.kmpmaps.features.core.presentation.components.getScreenHeight
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi

@Composable
fun PoiInfoBottomSheetContent(
    showLoading:Boolean = false,
    currentMarker: MarkerUi,
    onDirectionsClicked: () -> Unit,
    onBookmarkMarkerClicked: () -> Unit,
    onRemoveMarkerClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    Column(
        Modifier.height((getScreenHeight().value * 0.35).dp)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Row {
            Text(
                modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically),
                text = currentMarker.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    onCloseClicked.invoke()
                }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = currentMarker.address)
        Spacer(modifier = Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            KmpButton(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.directions),
                showLoading = showLoading,
                onClick = {
                    onDirectionsClicked.invoke()
                }
            )
            IconButton(
                onClick = {
                    clipboardManager.setText(
                        annotatedString = buildAnnotatedString {
                            append(text = currentMarker.googleUrl)
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
                    onBookmarkMarkerClicked.invoke()
                },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(
                        if (currentMarker.isBookMarked)
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
                text = currentMarker.latitude.toString() + ", " + currentMarker.longitude
            )
        }
        if (currentMarker.isBookMarked) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    onRemoveMarkerClicked.invoke()
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