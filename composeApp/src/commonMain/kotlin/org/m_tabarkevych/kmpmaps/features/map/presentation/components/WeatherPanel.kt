package org.m_tabarkevych.kmpmaps.features.map.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.m_tabarkevych.kmpmaps.features.weather.domain.model.WeatherData

@Composable
fun WeatherPanel(
    modifier: Modifier,
    weatherData: WeatherData
) {
    fun getCurrentHourIndex(): Int {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return currentTime.hour
    }

    Card(
        modifier = modifier.height(48.dp).width(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleMedium,
                text = weatherData.hourly.temperature_2m[getCurrentHourIndex()].toString() + "°C"
            )
        }
    }
}
