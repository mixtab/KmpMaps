package org.m_tabarkevych.kmpmaps.features.marker.domain.usecase

import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.toDomain


class BookmarkMarkerUseCase(
    private val markerRepository: IMarkerRepository
) {

    suspend operator fun invoke(markerUi: MarkerUi) {
        markerRepository.upsertMarker(markerUi.toDomain())
    }
}