package org.m_tabarkevych.kmpmaps.features.marker.domain.usecase

import org.m_tabarkevych.kmpmaps.features.core.domain.map
import org.m_tabarkevych.kmpmaps.features.core.domain.onSuccess
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.toMarkerUi

class GetMarkerUseCase(
    private val markerRepository: IMarkerRepository
) {

    suspend operator fun invoke(id: String) = markerRepository.getMarker(id).map { it.toMarkerUi() }
}
