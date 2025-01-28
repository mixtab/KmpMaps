package org.m_tabarkevych.kmpmaps.features.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.m_tabarkevych.kmpmaps.app.NavRoute

@Composable
fun SettingsEffectHandler(
    effect: Flow<SettingsUiEffect>,
    navigate: (NavRoute) -> Unit
){
    LaunchedEffect(Unit){
        effect.collectLatest { effect ->
            when(effect) {
                is SettingsUiEffect.ShowMessage -> {}
                is SettingsUiEffect.OnBackClicked -> {
                    navigate.invoke(NavRoute.Back)
                }

            }
        }
    }

}