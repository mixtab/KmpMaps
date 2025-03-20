package org.m_tabarkevych.kmpmaps.features.map.presentation.model

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates

@Serializable
data class MarkerUi(
    val id: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val comment: String,
    val isBookMarked: Boolean
) {

    val googleUrl = "http://maps.google.com/maps?daddr=${latitude},${longitude}"

    val coordinates  = Coordinates(latitude,longitude)
    companion object {
        fun createMarker(
            latitude: Double,
            longitude: Double,
            title: String,
            address: String,
            comment: String
        ): MarkerUi {
            val timeStamp = Clock.System.now().toEpochMilliseconds()

            return MarkerUi(
                id = timeStamp.toString() + latitude.toString() + longitude.toString(),
                timestamp = timeStamp,
                latitude = latitude,
                longitude = longitude,
                title = title,
                address = address,
                comment = comment,
                isBookMarked = false
            )
        }
    }
}

