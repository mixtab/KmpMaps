package org.m_tabarkevych.kmpmaps.features.core.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object KmpMapsDatabaseConstructor: RoomDatabaseConstructor<KmpMapsDatabase> {
    override fun initialize(): KmpMapsDatabase
}