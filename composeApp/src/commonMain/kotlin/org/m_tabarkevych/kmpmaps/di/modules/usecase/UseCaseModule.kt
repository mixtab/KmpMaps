package org.m_tabarkevych.kmpmaps.di.modules.usecase

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetLanguageUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetThemeUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.GetDistanceUnitUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetLanguageUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetThemeUseCase
import org.m_tabarkevych.kmpmaps.features.settings.domain.usecase.SetDistanceUnitUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.BookmarkMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkersUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.DeleteMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.marker.domain.usecase.GetMarkerUseCase
import org.m_tabarkevych.kmpmaps.features.weather.domain.usecase.GetWeatherDataUseCase

val useCaseModule = module {
    singleOf(::GetLanguageUseCase)
    singleOf(::GetThemeUseCase)
    singleOf(::GetDistanceUnitUseCase)
    singleOf(::SetLanguageUseCase)
    singleOf(::SetThemeUseCase)
    singleOf(::SetDistanceUnitUseCase)
    singleOf(::BookmarkMarkerUseCase)
    singleOf(::GetMarkersUseCase)
    singleOf(::DeleteMarkerUseCase)
    singleOf(::GetMarkerUseCase)
    singleOf(::GetWeatherDataUseCase)
}