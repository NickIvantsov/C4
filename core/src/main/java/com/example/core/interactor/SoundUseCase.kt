package com.example.core.interactor

import com.example.core_utils.util.logging.SoundName
import com.example.repository.ISoundRepository
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class SoundUseCase @Inject constructor(private val repository: ISoundRepository) {

    fun getAssetFileDescriptorAsync(soundName: SoundName): Deferred<Int?> {
        return repository.getAssetFileDescriptorAsync(soundName)
    }

}