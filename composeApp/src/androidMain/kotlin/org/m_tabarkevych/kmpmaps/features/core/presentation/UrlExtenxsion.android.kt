package org.m_tabarkevych.kmpmaps.features.core.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import org.m_tabarkevych.kmpmaps.KmpMapsApplication
import org.m_tabarkevych.kmpmaps.features.map.domain.model.Coordinates
import androidx.core.net.toUri


actual fun launchNavigationByCoordinates(startCoordinates: Coordinates, endCoordinates: Coordinates) {
    try {
        val url = "google.navigation:q=${endCoordinates.lat},${endCoordinates.lng}"
        val openBrowserIntent = Intent(
            Intent.ACTION_VIEW,
            url.toUri()
        )
        openBrowserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        KmpMapsApplication.instance.startActivity(openBrowserIntent)
    } catch (ex: Exception) {
        Log.e("launchUrl", ex.message, ex)
    }
}