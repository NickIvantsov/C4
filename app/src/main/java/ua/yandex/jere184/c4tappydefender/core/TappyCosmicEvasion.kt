package ua.yandex.jere184.c4tappydefender.core

import android.app.Application
import timber.log.Timber

import timber.log.Timber.DebugTree

import ua.yandex.jere184.c4tappydefender.BuildConfig
import ua.yandex.jere184.c4tappydefender.logging.CrashReportingTree

class TappyCosmicEvasion : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}