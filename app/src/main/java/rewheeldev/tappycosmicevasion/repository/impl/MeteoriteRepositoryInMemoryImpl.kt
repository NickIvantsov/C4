package rewheeldev.tappycosmicevasion.repository.impl

import android.graphics.Bitmap
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import java.util.*
import javax.inject.Inject

class MeteoriteRepositoryInMemoryImpl @Inject constructor() : IMeteoriteRepository {

    private val animateImgArray = ArrayList<Bitmap>()

    override fun add(meteoriteImg: Bitmap) {
        animateImgArray.add(meteoriteImg)
    }

    override fun get(index: Int): Bitmap {
        return animateImgArray[index]
    }

    override fun getSize(): Int {
        return animateImgArray.size
    }
}