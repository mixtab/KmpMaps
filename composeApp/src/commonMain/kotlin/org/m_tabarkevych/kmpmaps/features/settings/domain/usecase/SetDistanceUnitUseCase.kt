package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase

import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class SetDistanceUnitUseCase(
    private val settingsRepository: ISettingsRepository
) {

    suspend operator fun invoke(distanceUnit: DistanceUnit) {
        settingsRepository.setDistanceUnit(distanceUnit)
    }
}