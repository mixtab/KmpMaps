package org.m_tabarkevych.kmpmaps.features.core.presentation.manager

import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language

expect class AppLocaleManager() {
    fun getLocale(): String
    fun setAppLanguage(language: Language)
}

