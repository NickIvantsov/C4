package com.example.core.interactor

import com.example.model.SpaceDust
import com.example.repository.ISpaceDustRepository
import javax.inject.Inject

class SpaceDustUseCase @Inject constructor(private val spaceDustRepository: ISpaceDustRepository) {
    fun add(spaceDust: SpaceDust) {
        spaceDustRepository.add(spaceDust)
    }

    fun getByIndex(index: Int): SpaceDust {
        return spaceDustRepository.getByIndex(index)
    }

    fun getSize(): Int {
        return spaceDustRepository.getSize()
    }

    fun getAll(): List<SpaceDust> {
        return spaceDustRepository.getAll()
    }

    fun deleteAll() {
        spaceDustRepository.deleteAll()
    }
}