package rewheeldev.tappycosmicevasion.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import rewheeldev.tappycosmicevasion.ui.fragments.game.GameFragment
import rewheeldev.tappycosmicevasion.ui.fragments.main.MainFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector()
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector()
    abstract fun contributeGameFragment(): GameFragment
}