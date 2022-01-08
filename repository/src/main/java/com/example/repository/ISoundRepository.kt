package com.example.repository

import kotlinx.coroutines.Deferred
import com.example.core_utils.util.logging.SoundName

interface ISoundRepository {
    fun getAssetFileDescriptorAsync(soundName: SoundName): Deferred<Int?>
}