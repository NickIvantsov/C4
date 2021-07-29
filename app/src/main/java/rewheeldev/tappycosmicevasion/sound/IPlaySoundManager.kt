package rewheeldev.tappycosmicevasion.sound

import com.example.core_utils.util.logging.SoundName

interface IPlaySoundManager {
    suspend fun play(soundName: com.example.core_utils.util.logging.SoundName,
                     leftVolume: Float = SoundManager.LEFT_VOLUME,
                     rightVolume: Float = SoundManager.RIGHT_VOLUME,
                     priority: Int = SoundManager.SOUND_PRIORITY,
                     loop: Int = SoundManager.SOUND_LOOP,
                     rate: Float = SoundManager.SOUND_RATE
    )
}