package org.m_tabarkevych.kmpmaps.features.weather.data.data_source

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.m_tabarkevych.kmpmaps.features.core.data.datastore.safeCall
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.weather.data.model.WeatherResponse


class WeatherDataSource(
    private val httpClient: HttpClient
) {


    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double
    ): DomainResult<WeatherResponse> {
        return safeCall<WeatherResponse> {
            httpClient.get(
                urlString = "$WEATHER_URL/v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl"
            ) {

                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("timezone","Europe/Berlin")
                parameter("forecast_days",1)
            }
        }
    }

    companion object {
        const val WEATHER_URL = "https://api.open-meteo.com"
    }

}