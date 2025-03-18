package org.m_tabarkevych.kmpmaps.di.modules.datasource

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.core.data.datastore.HttpClientFactory
import org.m_tabarkevych.kmpmaps.features.weather.data.data_source.WeatherDataSource

val dataSourceModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::WeatherDataSource)
}