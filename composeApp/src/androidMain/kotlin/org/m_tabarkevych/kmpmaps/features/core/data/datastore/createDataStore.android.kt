package org.m_tabarkevych.kmpmaps.features.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.m_tabarkevych.kmpmaps.KmpMapsApplication

actual fun getDataStore(storePath: String): DataStore<Preferences> {
    val context = requireNotNull(KmpMapsApplication.instance)
    return createDataStore(
        producePath = { context.filesDir.resolve(storePath).absolutePath }
    )
}