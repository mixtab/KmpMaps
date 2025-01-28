package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class GetLanguageUseCase(
    private val settingsPreferences: ISettingsRepository
) {

    operator fun invoke(): Flow<Language> = settingsPreferences.getLanguage()

}