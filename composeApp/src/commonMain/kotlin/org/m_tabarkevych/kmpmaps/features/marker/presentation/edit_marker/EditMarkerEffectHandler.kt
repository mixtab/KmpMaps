package org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.m_tabarkevych.kmpmaps.app.NavRoute


@Composable
fun EditMarkerEffectHandler(
    effect: Flow<EditMarkerUiEffect>,
    navigate: (NavRoute) -> Unit
) {

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                EditMarkerUiEffect.NavigateBack -> navigate(NavRoute.Back)
                is EditMarkerUiEffect.ShowMessage -> {

                }
            }
        }

    }
}