package org.m_tabarkevych.kmpmaps.app

import kotlinx.serialization.Serializable
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.marker.domain.model.Marker

sealed interface NavRoute {
    @Serializable
    data object Back : NavRoute

    @Serializable
    data object MainGraph : NavRoute

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object Menu : NavRoute

    @Serializable
    data object Settings : NavRoute

    @Serializable
    data class EditMarker(val markerId:String): NavRoute

    @Serializable
    data object Markers: NavRoute
}