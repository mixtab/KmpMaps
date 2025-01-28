package org.m_tabarkevych.kmpmaps.features.core.presentation

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun launchUrl(url: String) {
    val link = NSURL(string = url)
    if (UIApplication.sharedApplication.canOpenURL(link)) {
        UIApplication.sharedApplication.openURL(link)
    }
}