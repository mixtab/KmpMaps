package org.m_tabarkevych.kmpmaps.features.marker.domain.usecase

import kotlinx.coroutines.flow.map
import org.m_tabarkevych.kmpmaps.features.marker.domain.IMarkerRepository
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.mapper.toMarkerUiList

class GetMarkersUseCase (
   private val markerRepository: IMarkerRepository
){

    operator fun invoke() = markerRepository.getMarkers().map {
        it.toMarkerUiList()
    }
}