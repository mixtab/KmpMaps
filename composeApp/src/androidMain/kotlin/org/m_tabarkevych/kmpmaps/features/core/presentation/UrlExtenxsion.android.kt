package org.m_tabarkevych.kmpmaps.features.core.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import org.m_tabarkevych.kmpmaps.KmpMapsApplication


actual fun launchUrl(url: String) {
    try {
        val openBrowserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        openBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        KmpMapsApplication.instance.startActivity(openBrowserIntent)
    } catch (ex: Exception) {
        Log.e("launchUrl", ex.message, ex)
    }
}