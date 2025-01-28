package org.m_tabarkevych.kmpmaps.features.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.ic_plus
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.m_tabarkevych.kmpmaps.features.core.presentation.White

@Composable
@Preview
fun KmpButton(
    modifier: Modifier = Modifier,
    text: String,
    icon:DrawableResource? = null,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                tint = White,
                contentDescription = "Start ivon"
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, color = White)
    }
}
