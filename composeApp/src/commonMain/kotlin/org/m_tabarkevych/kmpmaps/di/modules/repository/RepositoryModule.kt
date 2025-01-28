package org.m_tabarkevych.kmpmaps.di.modules.repository

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.settings.data.repository.SettingsRepository
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

import org.m_tabarkevych.kmpmaps.features.marker.data.repository.MarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository

val repositoryModule = module {
    singleOf(::SettingsRepository).bind<ISettingsRepository>()
    singleOf(::MarkerRepository).bind<IMarkerRepository>()
}