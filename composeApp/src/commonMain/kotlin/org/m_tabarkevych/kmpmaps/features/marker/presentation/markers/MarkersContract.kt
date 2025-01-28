package org.m_tabarkevych.kmpmaps.features.marker.presentation.markers

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.markers_option_build_route
import kmpmaps.composeapp.generated.resources.markers_option_delete
import kmpmaps.composeapp.generated.resources.markers_option_edit
import kmpmaps.composeapp.generated.resources.markers_option_share
import org.jetbrains.compose.resources.StringResource
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEffect
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEvent
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiState
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapUiEffect
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi


data class MarkersUiState(
    val showLoading: Boolean = false,
    val markers: List<MarkerUi> = listOf(),
    val currentSelectedMarker: MarkerUi? = null,
    val bottomSheetVisible: Boolean = false,
) : UiState


enum class MarkerOption(val title: StringResource, val textColor: Color?) {
    BUILD_ROUTE(Res.string.markers_option_build_route, null),
    EDIT(Res.string.markers_option_edit, null),
    SHARE(Res.string.markers_option_share, null),
    DELETE(Res.string.markers_option_delete, Color.Red)
}

sealed interface MarkersUiEvent : UiEvent {
    data object OnBackClicked : MarkersUiEvent
    data object OnBottomSheetCloseClicked : MarkersUiEvent
    data class OnMarkerOptionsClicked(val markerUi: MarkerUi) : MarkersUiEvent
    data class OnMarkerOptionSelected(val markerUi: MarkerUi, val option: MarkerOption) :
        MarkersUiEvent
}

sealed interface MarkersUiEffect : UiEffect {
    data object NavigateBack : MarkersUiEffect
    data class NavigateToEditMarker(val markerId: String) : MarkersUiEffect
    data class CopyToClipboard(val text:String):MarkersUiEffect
    data class ShowMessage(val message: StringResource) : MarkersUiEffect
}