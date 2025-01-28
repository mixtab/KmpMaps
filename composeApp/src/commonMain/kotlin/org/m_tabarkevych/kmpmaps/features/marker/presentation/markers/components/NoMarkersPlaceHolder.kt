package org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_new_marker
import kmpmaps.composeapp.generated.resources.marker_empty_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoMarkersPlaceHolder() {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            Image(
                modifier = Modifier.size(160.dp).align(Alignment.CenterHorizontally),
                painter = painterResource(Res.drawable.ic_new_marker),
                contentDescription = "Marker placeholder"
            )
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.marker_empty_placeholder),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}