package com.example.repository

import com.example.model.SpaceDust

interface ISpaceDustRepository {
    fun add(spaceDust: SpaceDust)
    fun getByIndex(index: Int): SpaceDust
    fun getSize(): Int
    fun getAll(): List<SpaceDust>
    fun deleteAll()
}