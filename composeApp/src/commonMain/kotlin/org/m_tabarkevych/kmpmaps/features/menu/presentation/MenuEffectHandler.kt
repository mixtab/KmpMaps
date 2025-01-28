package org.m_tabarkevych.kmpmaps.features.menu.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MenuEffectHandler(
    effect: Flow<MenuUiEffect>
){
    LaunchedEffect(Unit){
        effect.collectLatest { effect ->
            when(effect) {
                is MenuUiEffect.ShowMessage -> {}
            }
        }
    }

}