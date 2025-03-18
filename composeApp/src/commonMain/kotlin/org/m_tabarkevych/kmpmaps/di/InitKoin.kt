package org.m_tabarkevych.kmpmaps.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.m_tabarkevych.kmpmaps.di.modules.database.databaseFactoryModule
import org.m_tabarkevych.kmpmaps.di.modules.client.clientModule
import org.m_tabarkevych.kmpmaps.di.modules.database.databaseModule
import org.m_tabarkevych.kmpmaps.di.modules.database.preferencesModule
import org.m_tabarkevych.kmpmaps.di.modules.datasource.dataSourceModule
import org.m_tabarkevych.kmpmaps.di.modules.delegate.delegateModule
import org.m_tabarkevych.kmpmaps.di.modules.manager.managerModule
import org.m_tabarkevych.kmpmaps.di.modules.repository.repositoryModule
import org.m_tabarkevych.kmpmaps.di.modules.usecase.useCaseModule
import org.m_tabarkevych.kmpmaps.di.modules.viewmodel.viewModelModule

fun initKoin(config: KoinAppDeclaration? = null) {

    val sharedModule = listOf(
        viewModelModule,
        delegateModule,
        useCaseModule,
        databaseFactoryModule,
        databaseModule,
        clientModule,
        managerModule,
        repositoryModule,
        preferencesModule,
        dataSourceModule,
    )

    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}