package org.m_tabarkevych.kmpmaps.features.core.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Toolbar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    alwaysLight: Boolean = false
) {


    Column(modifier = modifier.statusBarsPadding().fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            IconButton(onClick = {
                onBackClick.invoke()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null,
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (alwaysLight) return@Column

       /* if (isSystemInDarkTheme()) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }*/
    }
}

@Composable
@Preview
private fun ToolbarPreview() {
    Toolbar("Title", {}, modifier = Modifier.fillMaxWidth())
}

@Composable
@Preview
private fun ToolbarAlwaysLightPreview() {
    Toolbar("Title", {}, modifier = Modifier.fillMaxWidth(), alwaysLight = true)
}