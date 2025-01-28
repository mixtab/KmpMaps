package org.m_tabarkevych.kmpmaps.features.marker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.marker.data.database.dao.MarkerDao
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.Marker
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.entityToDomain
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.toEntity

class MarkerRepository(
    private val markerDao: MarkerDao
) : IMarkerRepository {

    override fun getMarkers(): Flow<List<Marker>> {
        return markerDao.getMarkers().map { markers ->
            markers.map { it.entityToDomain() }
        }
    }

    override suspend fun getMarker(id: String): DomainResult<Marker, DataError.Remote> {
        return DomainResult.Success(markerDao.getMarker(id).entityToDomain())
    }

    override suspend fun deleteMarker(id: String): DomainResult<Unit, DataError.Remote> {
        return DomainResult.Success(markerDao.deleteMarker(id))
    }

    override suspend fun upsertMarker(marker: Marker): DomainResult<Unit, DataError.Remote> {
        return DomainResult.Success(markerDao.upsert(marker.toEntity()))
    }

}