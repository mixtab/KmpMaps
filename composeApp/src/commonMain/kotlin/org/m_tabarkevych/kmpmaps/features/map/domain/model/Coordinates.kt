package org.m_tabarkevych.kmpmaps.features.map.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lat: Double,
    val lng: Double
)