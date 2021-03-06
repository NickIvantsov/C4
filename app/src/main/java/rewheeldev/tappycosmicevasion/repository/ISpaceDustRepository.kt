package rewheeldev.tappycosmicevasion.repository

import rewheeldev.tappycosmicevasion.model.SpaceDust

interface ISpaceDustRepository {
    fun add(spaceDust: SpaceDust)
    fun getByIndex(index: Int): SpaceDust
    fun getSize(): Int
    fun getAll(): List<SpaceDust>
    fun deleteAll()
}