package rewheeldev.tappycosmicevasion.di.modules

import com.example.feature_game.sound.IPlaySoundManager
import com.example.feature_game.sound.SoundManager
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class SoundManagerModule {

    @Binds
    @Singleton
    abstract fun bindPlaySoundManager(manager: SoundManager): IPlaySoundManager
}