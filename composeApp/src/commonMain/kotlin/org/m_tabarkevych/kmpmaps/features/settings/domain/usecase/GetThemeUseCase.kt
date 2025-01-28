package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class GetThemeUseCase(
    private val settingsPreferences: ISettingsRepository
) {

    operator fun invoke(): Flow<Theme> {
        return settingsPreferences.getTheme()
    }
}