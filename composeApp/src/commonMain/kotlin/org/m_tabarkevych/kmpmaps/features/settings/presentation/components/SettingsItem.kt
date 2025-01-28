package org.m_tabarkevych.kmpmaps.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.features.core.presentation.extensions.clickableNoRipple

@Composable
@Preview
fun SettingItem(
    modifier: Modifier,
    title: String,
    selectedValue: String,
    icon: Painter,
    onClicked: () -> Unit,
) {
    Row(
        modifier = modifier.clickableNoRipple {
            onClicked.invoke()
        }.padding(horizontal = 16.dp).fillMaxWidth()

    ) {
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = icon,
            contentDescription = "Item icon"
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = title
        )
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 16.dp)
                .alpha(0.5f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            text = selectedValue
        )
        Icon(
            modifier = Modifier.align(Alignment.CenterVertically),
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "Arrow icon"
        )
    }
}