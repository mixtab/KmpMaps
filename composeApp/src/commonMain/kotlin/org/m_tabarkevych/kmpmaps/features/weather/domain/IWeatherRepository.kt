package org.m_tabarkevych.kmpmaps.features.weather.domain

import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.WeatherData

interface IWeatherRepository {

    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): DomainResult<WeatherData>

}