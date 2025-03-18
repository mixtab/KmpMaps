package org.m_tabarkevych.kmpmaps.di.modules.database

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.core.data.database.DatabaseFactory

actual val databaseFactoryModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
    }