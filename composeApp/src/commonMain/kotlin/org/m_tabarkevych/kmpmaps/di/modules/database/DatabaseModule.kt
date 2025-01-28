package org.m_tabarkevych.kmpmaps.di.modules.database

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.core.data.database.DatabaseFactory
import org.m_tabarkevych.kmpmaps.features.core.data.database.KmpMapsDatabase

val databaseModule = module {
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<KmpMapsDatabase>().markerDao }
}