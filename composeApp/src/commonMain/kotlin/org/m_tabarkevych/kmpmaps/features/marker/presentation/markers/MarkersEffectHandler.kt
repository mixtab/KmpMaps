package org.m_tabarkevych.kmpmaps.features.marker.presentation.markers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.presentation.showToast

@Composable
fun MarkersEffectHandler(
    effect: Flow<MarkersUiEffect>,
    navigate: (NavRoute) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
            when (effect) {
                MarkersUiEffect.NavigateBack -> navigate(NavRoute.Back)
                is MarkersUiEffect.NavigateToEditMarker ->
                    navigate.invoke(NavRoute.EditMarker(effect.markerId))

                is MarkersUiEffect.CopyToClipboard -> {
                    clipboardManager.setText(
                        annotatedString = buildAnnotatedString {
                            append(text = effect.text)
                        }
                    )
                }
                is MarkersUiEffect.ShowMessage -> {
                    showToast(getString(effect.message))
                }
            }

        }
    }
}