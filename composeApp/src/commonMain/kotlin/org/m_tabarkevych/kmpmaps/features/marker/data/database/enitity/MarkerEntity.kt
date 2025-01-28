package org.m_tabarkevych.kmpmaps.features.marker.data.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val comment: String
)