package org.m_tabarkevych.kmpmaps.features.settings.data.local.preferences

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.m_tabarkevych.kmpmaps.features.core.data.datastore.getDataStore
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme

class SettingsPreferences() {

    private val dataStore by lazy {
        getDataStore(PREFERENCES_NAME)
    }

    suspend fun setLanguage(language: Language) {
        dataStore.edit { it[LANGUAGE_KEY] = language.name }
    }

    fun getLanguage(): Flow<Language> {
        return dataStore.data.map { Language.valueOf(it[LANGUAGE_KEY] ?: Language.ENGLISH.name) }
    }

    suspend fun setTheme(theme: Theme) {
        dataStore.edit { it[THEME_KEY] = theme.name }
    }

    fun getTheme(): Flow<Theme> {
        return dataStore.data.map { Theme.valueOf(it[THEME_KEY] ?: Theme.FOLLOW_SYSTEM.name) }
    }

    suspend fun setDistanceUnit(distanceUnit: DistanceUnit) {
        dataStore.edit { it[DISTANCE_UNIT_KEY] = distanceUnit.name }
    }

    fun getDistanceUnit(): Flow<DistanceUnit> {
        return dataStore.data.map {
            DistanceUnit.valueOf(
                it[DISTANCE_UNIT_KEY] ?: DistanceUnit.KILOMETERS.name
            )
        }
    }


    companion object {
        const val PREFERENCES_NAME = "settings"

        private val LANGUAGE_KEY = stringPreferencesKey("LANGUAGE_KEY")
        private val THEME_KEY = stringPreferencesKey("THEME_KEY")
        private val DISTANCE_UNIT_KEY = stringPreferencesKey("DISTANCE_UNIT_KEY")
    }

}