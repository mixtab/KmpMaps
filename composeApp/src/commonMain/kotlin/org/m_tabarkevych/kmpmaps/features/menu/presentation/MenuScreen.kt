package org.m_tabarkevych.kmpmaps.features.menu.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.settings.presentation.SettingsViewModel


@Composable
fun MenuScreenRoute(viewModel: MenuViewModel, navigate: (NavRoute) -> Unit) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val effect = viewModel.effect.collectAsStateWithLifecycle(null)


    MenuEffectHandler(viewModel.effect)

    MenuScreenRoute(
        uiState = state.value,
        processUiEvent = {
            viewModel.sendUiEvent(it)
        }
    )
}

@Composable
fun MenuScreenRoute(
    uiState: MenuUiState,
    processUiEvent: (MenuUiEvent) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) { }

}