package rewheeldev.tappycosmicevasion.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import rewheeldev.tappycosmicevasion.ui.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity
}