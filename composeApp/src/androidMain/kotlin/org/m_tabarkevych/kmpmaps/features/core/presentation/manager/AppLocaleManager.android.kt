package org.m_tabarkevych.kmpmaps.features.core.presentation.manager

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import org.m_tabarkevych.kmpmaps.KmpMapsApplication
import org.m_tabarkevych.kmpmaps.features.settings.domain.model.Language
import java.util.Locale


actual class AppLocaleManager actual constructor() {

    actual fun getLocale(): String {
       return Locale.getDefault().language
    }

    actual fun setAppLanguage(language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)

    }
}