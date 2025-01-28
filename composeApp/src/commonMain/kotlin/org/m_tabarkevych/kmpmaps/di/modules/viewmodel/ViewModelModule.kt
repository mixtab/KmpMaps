package org.m_tabarkevych.kmpmaps.di.modules.viewmodel

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.app.MainViewModel
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapViewModel
import org.m_tabarkevych.kmpmaps.features.settings.presentation.SettingsViewModel
import org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker.EditMarkerViewModel
import org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.MarkersViewModel

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::EditMarkerViewModel)
    viewModelOf(::MarkersViewModel)
}