package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase


import org.m_tabarkevych.kmpmaps.features.core.presentation.manager.AppLocaleManager
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class SetLanguageUseCase(
    private val settingsRepository: ISettingsRepository
) {

    suspend operator fun invoke(language: Language) {
        AppLocaleManager().setAppLanguage(language)
        settingsRepository.setLanguage(language)
    }
}