package org.m_tabarkevych.kmpmaps.features.settings.data.repository

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.settings.data.local.preferences.SettingsPreferences
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class SettingsRepository(
    private val settingsPreferences: SettingsPreferences
) : ISettingsRepository {

    override suspend fun setLanguage(language: Language) {
        settingsPreferences.setLanguage(language)
    }

    override fun getLanguage(): Flow<Language> {
        return settingsPreferences.getLanguage()
    }

    override suspend fun setTheme(theme: Theme) {
        settingsPreferences.setTheme(theme)
    }

    override fun getTheme(): Flow<Theme> {
        return settingsPreferences.getTheme()
    }

    override suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        settingsPreferences.setDistanceUnit(distanceUnit)
    }

    override fun getDistanceUnit(): Flow<DistanceUnit> {
        return settingsPreferences.getDistanceUnit()
    }

}