package org.m_tabarkevych.kmpmaps.features.settings.domain.model


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.theme_dark
import kmpmaps.composeapp.generated.resources.theme_follow_system
import kmpmaps.composeapp.generated.resources.theme_light
import org.jetbrains.compose.resources.StringResource

enum class Theme (
    val title: StringResource,
){
    FOLLOW_SYSTEM(
        title = Res.string.theme_follow_system
    ),
    LIGHT(
        title = Res.string.theme_light
    ),
    DARK(
        title = Res.string.theme_dark
    )
}

@Composable
fun Theme.isDarkTheme() = when(this){
    Theme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT -> false
    Theme.DARK -> true
}