package rewheeldev.tappycosmicevasion.di.modules

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SoundPoolModule {
    @Provides
    @Singleton
    fun provideSoundPool(): SoundPool {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder()
                .setMaxStreams(10)
                .build();
        } else {
            SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
    }
}