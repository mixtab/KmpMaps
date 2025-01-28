package org.m_tabarkevych.kmpmaps.features.core.presentation

import androidx.compose.runtime.Composable
import platform.UIKit.UIAlertController
import platform.UIKit.UIApplication

actual fun showToast(text: String) {
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(
        UIAlertController().apply {
            title = null
            message = text
        },
        true,
        null
    )
}