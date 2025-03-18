package org.m_tabarkevych.kmpmaps.features.weather.domain.model.mapper

import org.m_tabarkevych.kmpmaps.features.weather.data.model.WeatherResponse
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.WeatherData


fun WeatherResponse.toDomainModel() = WeatherData(
    latitude = latitude,
    longitude = longitude,
    generationtime_ms = generationTimeMs,
    utc_offset_seconds = utcOffsetSeconds,
    timezone = timezone,
    timezone_abbreviation = timezoneAbbreviation,
    elevation = elevation,
    hourly_units = hourlyUnits.toDomainModel(),
    hourly = hourly.toDomainModel()
)

fun WeatherResponse.HourlyDto.toDomainModel() = WeatherData.HourlyWeatherDto(
    time = time,
    temperature_2m = temperature2m,
    weathercode = weathercode,
    relativehumidity_2m = relativeHumidity2m,
    windspeed_10m = windspeed10m,
    pressure_msl = pressureMsl
)

fun WeatherResponse.HourlyUnitsDto.toDomainModel() = WeatherData.HourlyUnitsDto(
    time = time,
    temperature_2m = temperature2m,
    weathercode = weathercode,
    relativehumidity_2m = relativeHumidity2m,
    windspeed_10m = windspeed10m,
    pressure_msl = pressureMsl
)