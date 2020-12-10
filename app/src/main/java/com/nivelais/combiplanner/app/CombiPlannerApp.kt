package com.nivelais.combiplanner.app

import android.app.Application
import com.nivelais.combiplanner.app.di.appModule
import com.nivelais.combiplanner.app.di.dataModule
import com.nivelais.combiplanner.app.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class, init Hilt with the help of the annotation
 */
class CombiPlannerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@CombiPlannerApp)
            modules(dataModule, domainModule, appModule)
        }
    }
}