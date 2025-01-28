package org.m_tabarkevych.kmpmaps.features.core.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<KmpMapsDatabase>
}