package org.m_tabarkevych.kmpmaps

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.m_tabarkevych.kmpmaps.di.initKoin

class KmpMapsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin {
            androidContext(this@KmpMapsApplication)
        }
    }

    companion object {
        lateinit var instance: KmpMapsApplication
            private set
    }
}