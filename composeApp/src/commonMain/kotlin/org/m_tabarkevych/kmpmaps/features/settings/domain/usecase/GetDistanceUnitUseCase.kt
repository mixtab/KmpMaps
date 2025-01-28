package org.m_tabarkevych.kmpmaps.features.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.DistanceUnit
import org.m_tabarkevych.kmpmaps.features.settings.domain.repository.ISettingsRepository

class GetDistanceUnitUseCase(
    private val settingsPreferences: ISettingsRepository
) {

    operator fun invoke(): Flow<DistanceUnit> {
        return settingsPreferences.getDistanceUnit()
    }
}