package org.m_tabarkevych.kmpmaps.features.marker.domain.usecase

import org.m_tabarkevych.kmpmaps.features.core.domain.DataError
import org.m_tabarkevych.kmpmaps.features.core.domain.DomainResult
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository

class DeleteMarkerUseCase(
    private val markerRepository: IMarkerRepository
) {
    suspend operator fun invoke(id: String): DomainResult<Unit> {
        return markerRepository.deleteMarker(id)
    }
}