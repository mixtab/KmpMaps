package org.m_tabarkevych.kmpmaps.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.m_tabarkevych.kmpmaps.features.core.presentation.DarkColorPalette
import org.m_tabarkevych.kmpmaps.features.core.presentation.LightColorPalette
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapScreenRoute
import org.m_tabarkevych.kmpmaps.features.map.presentation.MapViewModel
import org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker.EditMarkerRoute
import org.m_tabarkevych.kmpmaps.features.marker.presentation.edit_marker.EditMarkerViewModel
import org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.MarkersRoute
import org.m_tabarkevych.kmpmaps.features.marker.presentation.markers.MarkersViewModel
import org.m_tabarkevych.kmpmaps.features.menu.presentation.MenuScreenRoute
import org.m_tabarkevych.kmpmaps.features.menu.presentation.MenuViewModel
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Theme
import org.m_tabarkevych.kmpmaps.features.settings.presentation.SettingsScreenRoute
import org.m_tabarkevych.kmpmaps.features.settings.presentation.SettingsViewModel

val LocalLocalization = staticCompositionLocalOf { Language.UKRAINE.code }

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<MainViewModel>()

    val lanugage = viewModel.language.collectAsState().value
    CompositionLocalProvider(
        LocalLocalization provides lanugage.code,
    ) {

        val theme = viewModel.appTheme.collectAsStateWithLifecycle().value
        val isDarkTheme = when (theme) {
            Theme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            Theme.LIGHT -> false
            Theme.DARK -> true
        }


        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColorPalette else LightColorPalette
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavRoute.MainGraph
            ) {
                navigation<NavRoute.MainGraph>(
                    startDestination = NavRoute.Home
                ) {
                    composable<NavRoute.Home>(
                        enterTransition = { fadeIn() },
                        exitTransition = { fadeOut() }
                    ) {
                        val mapViewModel = koinViewModel<MapViewModel>()
                        MapScreenRoute(viewModel = mapViewModel, navigate = { direction ->
                            navController.navigate(direction)
                        })
                    }

                    composable<NavRoute.Menu>(
                        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
                        popEnterTransition = { slideInVertically(initialOffsetY = { it }) },
                    ) {
                        val menuViewModel = koinViewModel<MenuViewModel>()
                        MenuScreenRoute(viewModel = menuViewModel, navigate = { direction ->
                            navController.navigate(direction)
                        })
                    }

                    composable<NavRoute.Settings>(
                        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
                        popEnterTransition = { slideInVertically(initialOffsetY = { it }) },
                    ) {
                        val settingsViewModel = koinViewModel<SettingsViewModel>()
                        SettingsScreenRoute(viewModel = settingsViewModel, navigate = { direction ->
                            if (direction == NavRoute.Back) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(direction)
                            }
                        })
                    }
                    composable<NavRoute.EditMarker>(
                        enterTransition = { slideInVertically() },
                        popEnterTransition = { slideInVertically() },
                        exitTransition = { fadeOut() },
                        popExitTransition = { fadeOut() },
                    ) {
                        val settingsViewModel = koinViewModel<EditMarkerViewModel>()
                        EditMarkerRoute(viewModel = settingsViewModel, navigate = { direction ->
                            if (direction == NavRoute.Back) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(direction)
                            }
                        })
                    }

                    composable<NavRoute.Markers>{
                        val markersViewModel = koinViewModel<MarkersViewModel>()
                        MarkersRoute(viewModel = markersViewModel, navigate = { direction ->
                            if (direction == NavRoute.Back) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(direction)
                            }
                        })
                    }
                }
            }

        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}