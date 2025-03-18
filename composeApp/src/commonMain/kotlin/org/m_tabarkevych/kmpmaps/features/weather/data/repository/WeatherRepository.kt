package org.m_tabarkevych.kmpmaps.features.weather.data.repository

import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.core.domain.map
import org.m_tabarkevych.kmpmaps.features.weather.data.data_source.WeatherDataSource
import org.m_tabarkevych.kmpmaps.features.weather.domain.IWeatherRepository
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.WeatherData
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.mapper.toDomainModel

class WeatherRepository(
    private val weatherDataSource: WeatherDataSource
) : IWeatherRepository {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): DomainResult<WeatherData> =
         weatherDataSource.getWeatherData(latitude, longitude).map { it.toDomainModel() }

}
