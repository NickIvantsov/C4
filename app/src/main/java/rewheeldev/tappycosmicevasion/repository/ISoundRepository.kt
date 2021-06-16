package rewheeldev.tappycosmicevasion.repository

import kotlinx.coroutines.Deferred
import rewheeldev.tappycosmicevasion.util.SoundName

interface ISoundRepository {
    fun getAssetFileDescriptorAsync(soundName: SoundName): Deferred<Int?>
}