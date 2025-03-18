package org.m_tabarkevych.kmpmaps.features.map.presentation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.m_tabarkevych.kmpmaps.app.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapUiEffectHandler(
    effect: Flow<MapUiEffect>,
    bottomSheetState: SheetState,
    drawerState: DrawerState,
    navigate: (NavRoute) -> Unit,
) {
    LaunchedEffect(Unit) {
        effect.collect {
            when (it) {
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
                    launch { drawerState.close() }
                }

                MapUiEffect.ExpandBottomSheet -> {
                    launch {
                        bottomSheetState.expand()
                    }
                }

                MapUiEffect.PartialExpandBottomSheet -> {
                    launch {
                        bottomSheetState.partialExpand()
                    }
                }

                MapUiEffect.ShowMenu -> {
                    launch { drawerState.open() }
                }

                is MapUiEffect.NavigateToEditMarker ->
                    navigate.invoke(NavRoute.EditMarker(it.markerId))

            }
        }
    }
}
