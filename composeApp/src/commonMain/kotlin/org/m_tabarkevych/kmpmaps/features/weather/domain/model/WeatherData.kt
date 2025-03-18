package org.m_tabarkevych.kmpmaps.features.weather.domain.model

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val hourly_units: HourlyUnitsDto,
    val hourly: HourlyWeatherDto
) {

    data class HourlyUnitsDto(
        val time: String,
        val temperature_2m: String,
        val weathercode: String,
        val relativehumidity_2m: String,
        val windspeed_10m: String,
        val pressure_msl: String
    )

    data class HourlyWeatherDto(
        val time: List<String>,
        val temperature_2m: List<Double>,
        val weathercode: List<Int>,
        val relativehumidity_2m: List<Int>,
        val windspeed_10m: List<Double>,
        val pressure_msl: List<Double>
    )
}