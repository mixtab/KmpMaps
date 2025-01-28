package org.m_tabarkevych.kmpmaps.features.settings.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.features.core.presentation.MVIViewModel
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetDistanceUnitUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetLanguageUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetThemeUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetDistanceUnitUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetLanguageUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetThemeUseCase

class SettingsViewModel(
    private val setThemeUseCase: SetThemeUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setDistanceUnitUseCase: SetDistanceUnitUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getDistanceUnitUseCase: GetDistanceUnitUseCase,

    ) : MVIViewModel<SettingsUiState, SettingsUiEvent, SettingsUiEffect>() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    override val uiState = combine(
        _uiState,
        getLanguageUseCase.invoke(),
        getThemeUseCase.invoke(),
        getDistanceUnitUseCase.invoke()
    ) { state, language, theme, distanceInit ->
        state.copy(
            selectedLanguage = language,
            selectedTheme = theme,
            selectedDistanceUnit = distanceInit
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _uiState.value)

    override fun processUiEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.OnBackClicked -> setUiEffect { SettingsUiEffect.OnBackClicked }

            SettingsUiEvent.OnLanguageClicked -> handleLanguageClick()
            SettingsUiEvent.OnThemeClicked -> handleThemeClick()
            SettingsUiEvent.OnDistanceUnitClicked -> handleDistanceUnitClick()

            is SettingsUiEvent.OnItemSelected -> handleSelectedItemByIndex(event.index)

            SettingsUiEvent.OnBottomSheetCloseClicked -> handleCloseBottomSheet()

            SettingsUiEvent.OnLogoutClicked -> {

            }

        }
    }

    private fun handleLanguageClick() {
        _uiState.update {
            it.copy(
                bottomSheetVisible = true,
                bottomSheetType = SettingsBottomSheetType.LANGUAGE
            )
        }
    }

    private fun handleThemeClick() {
        _uiState.update {
            it.copy(
                bottomSheetVisible = true,
                bottomSheetType = SettingsBottomSheetType.THEME
            )
        }
    }

    private fun handleDistanceUnitClick() {
        _uiState.update {
            it.copy(
                bottomSheetVisible = true,
                bottomSheetType = SettingsBottomSheetType.DISTANCE_UNIT
            )
        }
    }

    private fun handleSelectedItemByIndex(index:Int) {
        viewModelScope.launch {
            when(currentState.bottomSheetType){
                SettingsBottomSheetType.LANGUAGE ->   setLanguageUseCase.invoke(Language.entries[index])
                SettingsBottomSheetType.THEME -> setThemeUseCase.invoke(Theme.entries[index])
                SettingsBottomSheetType.DISTANCE_UNIT -> setDistanceUnitUseCase.invoke(DistanceUnit.entries[index])
            }
        }
    }



    private fun handleCloseBottomSheet() {
        _uiState.update { it.copy(bottomSheetVisible = false) }
    }
}