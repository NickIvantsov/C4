package rewheeldev.tappycosmicevasion.di

import dagger.Binds
import dagger.Module
import rewheeldev.tappycosmicevasion.sound.IPlaySoundManager
import rewheeldev.tappycosmicevasion.sound.SoundManager
import javax.inject.Singleton

@Module
abstract class SoundManagerModule {

    @Binds
    @Singleton
    abstract fun bindPlaySoundManager(manager: SoundManager): IPlaySoundManager
}