package org.m_tabarkevych.kmpmaps.di.modules.delegate

import org.koin.core.module.dsl.singleOf
import org.m_tabarkevych.kmpmaps.features.map.presentation.delegate.BuildRouteDelegate
import org.koin.dsl.module

val delegateModule = module {
    singleOf(::BuildRouteDelegate)
}