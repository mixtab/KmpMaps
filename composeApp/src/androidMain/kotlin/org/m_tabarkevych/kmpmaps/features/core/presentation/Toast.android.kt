package org.m_tabarkevych.kmpmaps.features.core.presentation

import android.widget.Toast
import org.m_tabarkevych.kmpmaps.KmpMapsApplication

actual fun showToast(text: String) {

    Toast.makeText(KmpMapsApplication.instance, text, Toast.LENGTH_LONG).show()
}

