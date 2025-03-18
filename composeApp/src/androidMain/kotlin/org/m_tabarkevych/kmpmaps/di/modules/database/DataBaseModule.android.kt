package org.m_tabarkevych.kmpmaps.di.modules.database

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.core.data.database.DatabaseFactory

actual val databaseFactoryModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { DatabaseFactory(androidApplication()) }
}