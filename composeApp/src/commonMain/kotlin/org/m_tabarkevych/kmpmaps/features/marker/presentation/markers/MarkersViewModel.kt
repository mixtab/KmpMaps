package org.m_tabarkevych.kmpmaps.features.marker.presentation.markers

import androidx.lifecycle.viewModelScope
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.marker_deleted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel
import org.m_tabarkevych.kmpmaps.features.map.presentation.delegate.BuildRouteDelegate
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.DeleteMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkersUseCase

class MarkersViewModel(
    getMarkersUseCase: GetMarkersUseCase,
    private val deleteMarkerUseCase: DeleteMarkerUseCase,
    private val buildRouteDelegate: BuildRouteDelegate
) : MVIViewModel<MarkersUiState, MarkersUiEvent, MarkersUiEffect>() {

    private val _uiState = MutableStateFlow(MarkersUiState())
    override val uiState: StateFlow<MarkersUiState> =
        _uiState.combine(getMarkersUseCase.invoke()) { uiState, markers ->
            uiState.copy(markers = markers)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), _uiState.value)

    override fun processUiEvent(event: MarkersUiEvent) {
        when (event) {
            is MarkersUiEvent.OnBackClicked -> {
                setUiEffect { MarkersUiEffect.NavigateBack }
            }

            MarkersUiEvent.OnBottomSheetCloseClicked ->
                _uiState.update { it.copy(bottomSheetVisible = false) }

            is MarkersUiEvent.OnMarkerOptionSelected ->
                handleSelectedMarkerOption(event.markerUi, event.option)

            is MarkersUiEvent.OnMarkerOptionsClicked ->
                showMarkerOptions(event.markerUi)
        }
    }

    private fun handleSelectedMarkerOption(marker: MarkerUi, option: MarkerOption) {
        viewModelScope.launch {
            hideBottomSheet()

            when (option) {
                MarkerOption.BUILD_ROUTE -> {
                    setUiEffect { MarkersUiEffect.NavigateBack }
                    buildRouteDelegate.calculateRouteFromUserPosition(marker.coordinates)
                }

                MarkerOption.EDIT -> {
                    setUiEffect { MarkersUiEffect.NavigateToEditMarker(marker.id) }
                }

                MarkerOption.SHARE -> {
                    val link =
                        "http://maps.google.com/maps?daddr=${marker.latitude},${marker.longitude}"
                    setUiEffect { MarkersUiEffect.CopyToClipboard(link) }
                }

                MarkerOption.DELETE -> {
                    deleteMarkerUseCase.invoke(marker.id).onSuccess {
                        setUiEffect { MarkersUiEffect.ShowMessage(Res.string.marker_deleted) }
                    }
                }
            }
        }
    }

    private fun hideBottomSheet() =
        _uiState.update { it.copy(bottomSheetVisible = false) }

    private fun showMarkerOptions(marker: MarkerUi) {
        _uiState.update {
            it.copy(
                currentSelectedMarker = marker,
                bottomSheetVisible = true
            )
        }
    }
}