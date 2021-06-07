package ua.yandex.jere184.c4tappydefender.core

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import ua.yandex.jere184.c4tappydefender.BuildConfig
import ua.yandex.jere184.c4tappydefender.di.DaggerAppComponent
import ua.yandex.jere184.c4tappydefender.logging.CrashReportingTree
import javax.inject.Inject


class TappyCosmicEvasionApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

  }