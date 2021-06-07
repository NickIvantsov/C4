package ua.yandex.jere184.c4tappydefender.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ua.yandex.jere184.c4tappydefender.ui.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity
}