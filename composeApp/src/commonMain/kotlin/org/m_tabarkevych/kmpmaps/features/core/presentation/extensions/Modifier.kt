package org.m_tabarkevych.kmpmaps.features.core.presentation.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.clickableNoRipple(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}