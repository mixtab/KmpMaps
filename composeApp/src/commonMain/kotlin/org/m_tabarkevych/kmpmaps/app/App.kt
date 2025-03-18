package org.m_tabarkevych.kmpmaps.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    val language = viewModel.language.collectAsState()
    CompositionLocalProvider(LocalLocalization provides language.value.code) {

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
                startDestination = NavRoute.MainGraph,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
                popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
                popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
            ) {
                navigation<NavRoute.MainGraph>(
                    startDestination = NavRoute.Home
                ) {
                    composable<NavRoute.Home> {
                        val mapViewModel = koinViewModel<MapViewModel>()
                        MapScreenRoute(viewModel = mapViewModel, navigate = { direction ->
                            navController.navigate(direction)
                        })
                    }

                    composable<NavRoute.Menu> {
                        val menuViewModel = koinViewModel<MenuViewModel>()
                        MenuScreenRoute(viewModel = menuViewModel, navigate = { direction ->
                            navController.navigate(direction)
                        })
                    }

                    composable<NavRoute.Settings> {
                        val settingsViewModel = koinViewModel<SettingsViewModel>()
                        SettingsScreenRoute(viewModel = settingsViewModel, navigate = { direction ->
                            if (direction == NavRoute.Back) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(direction)
                            }
                        })
                    }
                    composable<NavRoute.EditMarker> {
                        val settingsViewModel = koinViewModel<EditMarkerViewModel>()
                        EditMarkerRoute(viewModel = settingsViewModel, navigate = { direction ->
                            if (direction == NavRoute.Back) {
                                navController.navigateUp()
                            } else {
                                navController.navigate(direction)
                            }
                        })
                    }

                    composable<NavRoute.Markers> {
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

