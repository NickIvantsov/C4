package ua.yandex.jere184.c4tappydefender.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import ua.yandex.jere184.c4tappydefender.core.TappyCosmicEvasionApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ApiModule::class,
        ViewModelBuilderModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }


    /*
     * This is our custom Application class
     * */
    fun inject(appController: TappyCosmicEvasionApplication)
}
