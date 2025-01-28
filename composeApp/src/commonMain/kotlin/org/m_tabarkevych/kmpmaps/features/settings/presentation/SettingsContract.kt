package org.m_tabarkevych.kmpmaps.features.settings.presentation

import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEffect
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiEvent
import org.m_tabarkevych.kmpmaps.features.core.presentation.UiState
import org.m_tabarkevych.kmpmaps.features.menu.presentation.MenuUiEffect
import org.m_tabarkevych.kmpmaps.features.menu.presentation.MenuUiEvent
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme


data class SettingsUiState(
    val bottomSheetType: SettingsBottomSheetType = SettingsBottomSheetType.LANGUAGE,
    val bottomSheetVisible: Boolean = false,

    val selectedLanguage: Language = Language.ENGLISH,
    val selectedTheme: Theme = Theme.FOLLOW_SYSTEM,
    val selectedDistanceUnit: DistanceUnit = DistanceUnit.KILOMETERS

) : UiState {

    val getBottomSheetItems = when (bottomSheetType) {
        SettingsBottomSheetType.LANGUAGE ->
            Language.entries.map { Pair(it.title, it == selectedLanguage) }

        SettingsBottomSheetType.THEME ->
            Theme.entries.map { Pair(it.title, it == selectedTheme) }

        SettingsBottomSheetType.DISTANCE_UNIT ->
            DistanceUnit.entries.map { Pair(it.title, it == selectedDistanceUnit) }
    }
}

sealed interface SettingsUiEvent : UiEvent {
    data object OnBackClicked : SettingsUiEvent

    data object OnLanguageClicked : SettingsUiEvent
    data object OnThemeClicked : SettingsUiEvent
    data object OnDistanceUnitClicked : SettingsUiEvent

    data class OnItemSelected(val index:Int): SettingsUiEvent

    data object OnBottomSheetCloseClicked : SettingsUiEvent

    data object OnLogoutClicked : SettingsUiEvent
}

sealed interface SettingsUiEffect : UiEffect {
    data class ShowMessage(val message: String) : SettingsUiEffect
    data object OnBackClicked : SettingsUiEffect

}