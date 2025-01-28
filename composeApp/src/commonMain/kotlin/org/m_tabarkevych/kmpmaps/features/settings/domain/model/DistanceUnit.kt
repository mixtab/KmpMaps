package org.m_tabarkevych.kmpmaps.features.settings.domain.model

import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.distance_unit_km
import kmpmaps.composeapp.generated.resources.distance_unit_miles
import org.jetbrains.compose.resources.StringResource

enum class DistanceUnit(val title: StringResource) {
    KILOMETERS(title = Res.string.distance_unit_km),
    MILES(title = Res.string.distance_unit_miles),
}