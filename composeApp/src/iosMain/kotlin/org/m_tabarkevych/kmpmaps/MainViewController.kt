package org.m_tabarkevych.kmpmaps

import androidx.compose.ui.window.ComposeUIViewController
import org.m_tabarkevych.kmpmaps.app.App
import org.m_tabarkevych.kmpmaps.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
