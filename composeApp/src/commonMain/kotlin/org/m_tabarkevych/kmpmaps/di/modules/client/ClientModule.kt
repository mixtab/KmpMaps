package org.m_tabarkevych.kmpmaps.di.modules.client

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.core.domain.client.LocationClient

val clientModule = module {
      singleOf(::LocationClient).bind<LocationClient>()
}