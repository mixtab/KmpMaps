package org.m_tabarkevych.kmpmaps.features.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.m_tabarkevych.kmpmaps.features.marker.data.database.dao.MarkerDao
import org.m_tabarkevych.kmpmaps.features.marker.data.database.enitity.MarkerEntity

@Database(
    entities = [MarkerEntity::class],
    version = 1
)
/*@TypeConverters(
    StringListTypeConverter::class
)*/
/*@ConstructedBy(KmpMapsDatabaseConstructor::class)*/
abstract class KmpMapsDatabase: RoomDatabase() {
    abstract val markerDao: MarkerDao

    companion object {
        const val DB_NAME = "maps.db"
    }
}