package org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.app.NavRoute
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.toMarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.BookmarkMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkerUseCase

class EditMarkerViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMarkerUseCase: GetMarkerUseCase,
    private val bookmarkMarkerUseCase: BookmarkMarkerUseCase
) : MVIViewModel<EditMarkerUiState, EditMarkerUiEvent, EditMarkerUiEffect>() {

    private val markerId = savedStateHandle.toRoute<NavRoute.EditMarker>().markerId

    private val _uiState = MutableStateFlow(EditMarkerUiState())
    override val uiState: StateFlow<EditMarkerUiState> = _uiState.asStateFlow().onStart {
        getMarkerUseCase.invoke(markerId).onSuccess {
            _uiState.value = _uiState.value.copy(
                marker = it.toMarkerUi(),
                titleInputValue = it.title,
                commentInputValue = it.comment
            )
        }

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _uiState.value)

    override fun processUiEvent(event: EditMarkerUiEvent) {
        when (event) {
            EditMarkerUiEvent.OnBackClicked -> setUiEffect { EditMarkerUiEffect.NavigateBack }

            is EditMarkerUiEvent.OnCommentChanged -> {
                _uiState.value = _uiState.value.copy(commentInputValue = event.comment)
            }

            is EditMarkerUiEvent.OnTitleChanged -> {
                _uiState.value = _uiState.value.copy(titleInputValue = event.title)
            }

            EditMarkerUiEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    bookmarkMarkerUseCase.invoke(currentState.marker ?: return@launch)
                    setUiEffect { EditMarkerUiEffect.NavigateBack }
                }
            }
        }
    }
}