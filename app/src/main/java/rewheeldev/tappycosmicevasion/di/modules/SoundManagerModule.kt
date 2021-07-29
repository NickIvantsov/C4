package rewheeldev.tappycosmicevasion.di.modules

import dagger.Binds
import dagger.Module
import com.example.feature_game.sound.IPlaySoundManager
import com.example.feature_game.sound.SoundManager
import javax.inject.Singleton

@Module
abstract class SoundManagerModule {

    @Binds
    @Singleton
    abstract fun bindPlaySoundManager(manager: com.example.feature_game.sound.SoundManager): com.example.feature_game.sound.IPlaySoundManager
}