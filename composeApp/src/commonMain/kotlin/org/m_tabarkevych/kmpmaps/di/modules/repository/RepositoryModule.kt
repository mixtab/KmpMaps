package org.m_tabarkevych.kmpmaps.di.modules.repository

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.settings.data.repository.SettingsRepository
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository
import org.m_tabarkevych.kmpmaps.features.weather.data.repository.WeatherRepository
import org.m_tabarkevych.kmpmaps.features.marker.data.repository.MarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository
import org.m_tabarkevych.kmpmaps.features.weather.domain.IWeatherRepository

val repositoryModule = module {
    singleOf(::WeatherRepository).bind<IWeatherRepository>()
    singleOf(::SettingsRepository).bind<ISettingsRepository>()
    singleOf(::MarkerRepository).bind<IMarkerRepository>()
}