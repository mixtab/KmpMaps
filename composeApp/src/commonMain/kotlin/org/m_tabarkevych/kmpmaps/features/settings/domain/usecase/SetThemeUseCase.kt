package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase

import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class SetThemeUseCase(
    private val settingsRepository: ISettingsRepository
) {
    suspend operator fun invoke(theme: Theme) {
        settingsRepository.setTheme(theme)
    }
}