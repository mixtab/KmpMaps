package org.m_tabarkevych.kmpmaps.features.settings.domain.repository

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme

interface ISettingsRepository {
    suspend fun setLanguage(language: Language)
    fun getLanguage(): Flow<Language>

    suspend fun setTheme(theme: Theme)
    fun getTheme(): Flow<Theme>

    suspend fun setDistanceUnit(distanceUnit: DistanceUnit)
    fun getDistanceUnit(): Flow<DistanceUnit>
}