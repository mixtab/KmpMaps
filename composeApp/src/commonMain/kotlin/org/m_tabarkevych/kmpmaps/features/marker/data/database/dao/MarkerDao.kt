package org.m_tabarkevych.kmpmaps.features.marker.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.marker.data.database.enitity.MarkerEntity

@Dao
interface MarkerDao {

    @Upsert
    suspend fun upsert(marker: MarkerEntity)

    @Query("SELECT * FROM MarkerEntity")
    fun getMarkers(): Flow<List<MarkerEntity>>

    @Query("SELECT * FROM MarkerEntity WHERE id = :id")
    suspend fun getMarkers(id: String): MarkerEntity

    @Query("SELECT * FROM MarkerEntity WHERE id = :id")
    suspend fun getMarker(id: String): MarkerEntity

    @Query("DELETE FROM MarkerEntity WHERE id = :id")
    suspend fun deleteMarker(id: String)
}