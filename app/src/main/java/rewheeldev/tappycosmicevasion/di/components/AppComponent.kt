package rewheeldev.tappycosmicevasion.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import rewheeldev.tappycosmicevasion.core.TappyCosmicEvasionApplication
import rewheeldev.tappycosmicevasion.di.*
import rewheeldev.tappycosmicevasion.di.modules.*
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ApiModule::class,
        ViewModelBuilderModule::class,
        ActivityModule::class,
        AndroidSupportInjectionModule::class,
        SharedPreferencesModule::class,
        RepositoryModule::class,
        FragmentModule::class,
        RoomModule::class,
        SoundPoolModule::class,
        SoundManagerModule::class,
        ContextModule::class,
        AssetManagerModule::class,
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
