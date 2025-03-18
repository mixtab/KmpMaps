package org.m_tabarkevych.kmpmaps.features.weather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTimeMs: Double,
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int,
    val timezone: String,
    @SerialName("timezone_abbreviation") val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName("hourly_units") val hourlyUnits: HourlyUnitsDto,
    val hourly: HourlyDto
) {

    @Serializable
    data class HourlyUnitsDto(
        val time: String,
        @SerialName("temperature_2m") val temperature2m: String,
        val weathercode: String,
        @SerialName("relativehumidity_2m") val relativeHumidity2m: String,
        @SerialName("windspeed_10m") val windspeed10m: String,
        @SerialName("pressure_msl") val pressureMsl: String
    )

    @Serializable
    data class HourlyDto(
        val time: List<String>,
        @SerialName("temperature_2m") val temperature2m: List<Double>,
        val weathercode: List<Int>,
        @SerialName("relativehumidity_2m") val relativeHumidity2m: List<Int>,
        @SerialName("windspeed_10m") val windspeed10m: List<Double>,
        @SerialName("pressure_msl") val pressureMsl: List<Double>
    )
}