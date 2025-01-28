package org.m_tabarkevych.kmpmaps.di.modules.manager

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.m_tabarkevych.kmpmaps.features.menu.domain.manager.PlacesManager


val managerModule = module {
    singleOf(::PlacesManager)
}