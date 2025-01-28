package org.m_tabarkevych.kmpmaps.features.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<KmpMapsDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(KmpMapsDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}