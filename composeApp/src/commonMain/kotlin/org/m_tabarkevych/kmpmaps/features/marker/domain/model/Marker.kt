package org.m_tabarkevych.kmpmaps.features.marker.domain.model

data class Marker (
    val id: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val comment: String
)