package org.m_tabarkevych.kmpmaps.features.weather.domain.usecase

import org.m_tabarkevych.kmpmaps.features.weather.domain.IWeatherRepository

class GetWeatherDataUseCase(
    private val repository: IWeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) =
        repository.getWeatherData(latitude, longitude)
}