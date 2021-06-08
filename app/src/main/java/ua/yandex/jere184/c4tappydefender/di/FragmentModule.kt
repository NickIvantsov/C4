package ua.yandex.jere184.c4tappydefender.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.yandex.jere184.c4tappydefender.ui.fragments.main.MainFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector()
    abstract fun contributeMainFragment(): MainFragment
}