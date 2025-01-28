package org.m_tabarkevych.kmpmaps.features.menu.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel

class MenuViewModel : MVIViewModel<MenuUiState, MenuUiEvent, MenuUiEffect>() {

    private val _uiState = MutableStateFlow(MenuUiState())
    override val uiState = _uiState.asStateFlow()

    override fun processUiEvent(event: MenuUiEvent) {
        when(event){
            MenuUiEvent.OnProfileClicked -> TODO()
        }
    }
}