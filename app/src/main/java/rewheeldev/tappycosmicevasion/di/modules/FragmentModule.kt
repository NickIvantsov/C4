package rewheeldev.tappycosmicevasion.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.example.feature_game.GameFragment
import rewheeldev.tappycosmicevasion.ui.fragments.main.MainFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector()
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector()
    abstract fun contributeGameFragment(): com.example.feature_game.GameFragment
}