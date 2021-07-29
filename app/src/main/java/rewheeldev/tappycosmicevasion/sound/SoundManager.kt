package rewheeldev.tappycosmicevasion.sound

import android.media.SoundPool
import com.example.repository.ISoundRepository
import javax.inject.Inject

class SoundManager @Inject constructor(
    private val soundRepository: ISoundRepository,
    private val soundPool: SoundPool
) : IPlaySoundManager {


    private fun playSound(
        it: Int,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) = soundPool.play(
        it,
        leftVolume,
        rightVolume,
        priority,
        loop,
        rate
    )


    override suspend fun play(
        soundName: com.example.core_utils.util.logging.SoundName,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) {
        playImpl(soundName, leftVolume, rightVolume, priority, loop, rate)
    }

    private suspend fun playImpl(
        soundName: com.example.core_utils.util.logging.SoundName,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) {
        soundRepository.getAssetFileDescriptorAsync(soundName).await()?.let {
            var streamID = -1
            do {
                streamID = playSound(it, leftVolume, rightVolume, priority, loop, rate)
            } while (streamID == 0)
        }
    }

    companion object {
        const val LEFT_VOLUME = 1f
        const val RIGHT_VOLUME = 1f
        const val SOUND_PRIORITY = 0
        const val SOUND_LOOP = 0
        const val SOUND_RATE = 1f
    }
}