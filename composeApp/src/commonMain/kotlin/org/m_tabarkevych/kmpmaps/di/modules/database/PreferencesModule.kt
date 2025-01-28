package org.m_tabarkevych.kmpmaps.di.modules.database

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.settings.data.local.preferences.SettingsPreferences

val preferencesModule = module {
    singleOf(::SettingsPreferences)
}