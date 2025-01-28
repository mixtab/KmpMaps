package org.m_tabarkevych.kmpmaps.features.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { (producePath() + ".preferences_pb").toPath() }
    )


expect fun getDataStore(storePath:String): DataStore<Preferences>

