package org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker

import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEffect
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEvent
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiState
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi


data class EditMarkerUiState(
    val showLoading: Boolean = false,
    val marker: MarkerUi? = null,
    val titleInputValue:String = "",
    val commentInputValue:String = "",
) : UiState

sealed interface EditMarkerUiEvent : UiEvent {
    data object OnBackClicked : EditMarkerUiEvent
    data object OnSaveClicked : EditMarkerUiEvent
    data class OnTitleChanged(val title: String) : EditMarkerUiEvent
    data class OnCommentChanged(val comment: String) : EditMarkerUiEvent
}

sealed interface EditMarkerUiEffect : UiEffect {
    data object NavigateBack : EditMarkerUiEffect
    data class ShowMessage(val message: String) : EditMarkerUiEffect
}