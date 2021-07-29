package rewheeldev.tappycosmicevasion.repository.impl

import com.example.model.SpaceDust
import rewheeldev.tappycosmicevasion.repository.ISpaceDustRepository
import java.util.*
import javax.inject.Inject

class SpaceDustRepositoryInMemoryImpl @Inject constructor() : ISpaceDustRepository {

    private var spaceDustList = ArrayList<SpaceDust>()

    override fun add(spaceDust: SpaceDust) {
        spaceDustList.add(spaceDust)
    }

    override fun getByIndex(index: Int): SpaceDust {
        return spaceDustList[index]
    }

    override fun getSize(): Int {
        return spaceDustList.size
    }

    override fun getAll(): List<SpaceDust> {
        return spaceDustList
    }

    override fun deleteAll() {
        spaceDustList.clear()
    }
}