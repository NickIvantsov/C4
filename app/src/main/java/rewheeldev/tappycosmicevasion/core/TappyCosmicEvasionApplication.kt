package rewheeldev.tappycosmicevasion.core

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import rewheeldev.tappycosmicevasion.BuildConfig
import rewheeldev.tappycosmicevasion.di.DaggerAppComponent
import rewheeldev.tappycosmicevasion.logging.CrashReportingTree
import rewheeldev.tappycosmicevasion.model.EnemyShip
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

        EnemyShip.initBitmap(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

}