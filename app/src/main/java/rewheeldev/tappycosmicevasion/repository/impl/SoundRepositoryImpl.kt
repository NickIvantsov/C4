package rewheeldev.tappycosmicevasion.repository.impl

import android.content.res.AssetManager
import android.media.SoundPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import rewheeldev.tappycosmicevasion.logging.logD
import rewheeldev.tappycosmicevasion.repository.ISoundRepository
import com.example.core_utils.util.logging.SoundName
import com.example.core_utils.util.logging.SoundName.*
import javax.inject.Inject

class SoundRepositoryImpl @Inject constructor(
    private val assetManager: AssetManager,
    private val soundPool: SoundPool
) : ISoundRepository {
    private val soundCashMap = HashMap<String, Int>()

    override fun getAssetFileDescriptorAsync(soundName: com.example.core_utils.util.logging.SoundName): Deferred<Int?> {
        return CoroutineScope(Dispatchers.IO).async {
            val fileName = getFileName(soundName)
            if (soundCashMap.containsKey(fileName)) {
                log("containsKey = ${soundCashMap.containsKey(fileName)}")
                soundCashMap[fileName]
            } else {
                addToCash(fileName)
                soundCashMap[fileName]
            }
        }
    }

    private fun addToCash(fileName: String) {
        soundCashMap[fileName] = soundPool.load(assetManager.openFd(fileName), 0)
    }

    private fun getFileName(soundName: com.example.core_utils.util.logging.SoundName): String {
        return when (soundName) {
            START -> START.fileName
            BUMP -> BUMP.fileName
            DESTROYED -> DESTROYED.fileName
        }
    }

    private fun log(msg: String) {
        logD(msg)
    }
}