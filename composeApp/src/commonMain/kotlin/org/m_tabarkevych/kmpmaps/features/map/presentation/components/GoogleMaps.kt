package org.m_tabarkevych.kmpmaps.features.map.presentation.components

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import org.m_tabarkevych.kmpmaps.features.map.domain.model.RouteInfo
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapAction
import org.m_tabarkevych.kmpmaps.features.map.presentation.model.MarkerUi
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme

@Composable
expect fun GoogleMaps(
    isSatelliteView:Boolean,
    theme: Theme,
    isMyLocationEnabled: Boolean,
    userLocation: Coordinates?,
    currentMarker:MarkerUi?,
    markers:List<MarkerUi>,
    routeInfo: RouteInfo?,
    mapActionSharedFlow: SharedFlow<MapAction>,
    onMapClicked:() -> Unit,
    onMapLongClicked:(Coordinates) -> Unit,
    onMarkerClicked:(MarkerUi) -> Unit
)
