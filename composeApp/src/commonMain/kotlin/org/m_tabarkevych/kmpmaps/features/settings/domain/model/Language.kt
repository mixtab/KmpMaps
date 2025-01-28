package org.m_tabarkevych.kmpmaps.features.settings.domain.model

import kmpmaps.composeapp.generated.resources.Res
import kmpmaps.composeapp.generated.resources.language_en
import kmpmaps.composeapp.generated.resources.language_ua
import org.jetbrains.compose.resources.StringResource

enum class Language(
    val title: StringResource,
    val code: String
) {
    UKRAINE(title = Res.string.language_ua, "ua"),
    ENGLISH(title = Res.string.language_en, "en")
}