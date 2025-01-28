package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.app.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapUiEffectHandler(
    effect: MapUiEffect?,
    bottomSheetState: SheetState,
    drawerState: DrawerState,
    navigate: (NavRoute) -> Unit,
) {
    val scope = rememberCoroutineScope()
    when (effect) {
        is MapUiEffect.ShowMessage -> {

        }

        is MapUiEffect.NavigateToMenu -> {
            navigate.invoke(NavRoute.Menu)
        }

        is MapUiEffect.NavigateToSettings -> {
            navigate.invoke(NavRoute.Settings)
        }
        is MapUiEffect.NavigateToMarkers -> {
            navigate.invoke(NavRoute.Markers)
        }

        MapUiEffect.HideMenu -> {
            scope.launch { drawerState.close() }
        }

        MapUiEffect.ExpandBottomSheet -> {
            scope.launch { bottomSheetState.expand() }
        }

        MapUiEffect.PartialExpandBottomSheet -> {
            scope.launch { bottomSheetState.partialExpand() }
        }

        MapUiEffect.ShowMenu -> {
            scope.launch { drawerState.open() }
        }

        is MapUiEffect.NavigateToEditMarker ->
            navigate.invoke(NavRoute.EditMarker(effect.markerId))

        null -> Unit
    }

}