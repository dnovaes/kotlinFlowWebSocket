package com.example.flowwebsocket

import android.app.Application
import com.example.flowwebsocket.data.dataModule
import com.example.flowwebsocket.features.featuresModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RoomFlowApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RoomFlowApplication)
            modules(dataModule + featuresModule)
        }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}
