package org.m_tabarkevych.kmpmaps.features.marker.domain

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.core.domain.Error
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.Marker

interface IMarkerRepository {

    fun getMarkers(): Flow<List<Marker>>

    suspend fun getMarker(id: String): DomainResult<Marker>

    suspend fun deleteMarker(id: String): DomainResult<Unit>

    suspend fun upsertMarker(marker: Marker): DomainResult<Unit>
}