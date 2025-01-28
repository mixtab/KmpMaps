package org.m_tabarkevych.kmpmaps.features.menu.presentation

import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEffect
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEvent
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiState

data class MenuUiState(
    val showLoading: Boolean = false
) : UiState

sealed interface MenuUiEvent : UiEvent {
    data object OnProfileClicked : MenuUiEvent
}

sealed interface MenuUiEffect : UiEffect {
    data class ShowMessage(val message: String) : MenuUiEffect
}