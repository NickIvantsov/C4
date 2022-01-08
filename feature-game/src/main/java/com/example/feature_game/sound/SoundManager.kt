package com.example.feature_game.sound

import android.media.SoundPool
import com.example.core.interactor.SoundUseCase
import com.example.core_utils.util.logging.SoundName
import javax.inject.Inject

class SoundManager @Inject constructor(
    private val soundUseCase: SoundUseCase,
    private val soundPool: SoundPool
) : IPlaySoundManager {


    private fun playSound(
        soundID: Int,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) = soundPool.play(
        soundID,
        leftVolume,
        rightVolume,
        priority,
        loop,
        rate
    )


    override suspend fun play(
        soundName: SoundName,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) {
        playImpl(soundName, leftVolume, rightVolume, priority, loop, rate)
    }

    private suspend fun playImpl(
        soundName: SoundName,
        leftVolume: Float,
        rightVolume: Float,
        priority: Int,
        loop: Int,
        rate: Float
    ) {
        soundUseCase.getAssetFileDescriptorAsync(soundName).await()?.let {
            var streamID: Int
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