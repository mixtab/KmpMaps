package org.m_tabarkevych.kmpmaps.features.core.presentation.manager

import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import platform.Foundation.valueForKey

actual class AppLocaleManager actual constructor() {

    private val defaults by lazy { NSUserDefaults.standardUserDefaults }

    actual fun getLocale(): String {
       return defaults.valueForKey(LANGUAGE_KEY).toString()
    }

    actual fun setAppLanguage(language: Language) {
        NSUserDefaults.standardUserDefaults.setObject(arrayListOf(language.code),LANGUAGE_KEY)
    }

    companion object {
        private const val LANGUAGE_KEY = "AppleLanguages"
    }
}