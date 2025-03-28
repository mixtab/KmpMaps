package org.m_tabarkevych.kmpmaps.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.m_tabarkevych.kmpmaps.features.core.presentation.manager.AppLocaleManager
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetLanguageUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetThemeUseCase

class MainViewModel(
    getThemeUseCase: GetThemeUseCase,
    getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    val appTheme =
        getThemeUseCase.invoke()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Theme.FOLLOW_SYSTEM)


    val language =
        getLanguageUseCase.invoke()
            .onEach { AppLocaleManager().setAppLanguage(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Language.ENGLISH)

}