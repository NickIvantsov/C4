package rewheeldev.tappycosmicevasion.repository

import android.graphics.Bitmap
import rewheeldev.tappycosmicevasion.logging.logD
import rewheeldev.tappycosmicevasion.model.Meteorite
import java.util.*
import javax.inject.Inject

class MeteoriteRepositoryInMemoryImpl @Inject constructor() : IMeteoriteRepository {

    private val animateImgArray = ArrayList<Bitmap>()
    private var meteoriteList = ArrayList<Meteorite>()

    override fun addMeteoriteBitmap(meteoriteImg: Bitmap) {
        animateImgArray.add(meteoriteImg)
    }

    override fun getMeteoriteBitmap(index: Int): Bitmap {
        return animateImgArray[index]
    }

    override fun getSizeOfMeteoriteBitmaps(): Int {
        return animateImgArray.size
    }

    override fun addMeteorite(meteorite: Meteorite) {
        meteoriteList.add(meteorite)
    }

    override fun getMeteoriteByIndex(index: Int): Meteorite {
        return meteoriteList[index]
    }

    override fun getSizeMeteoriteList(): Int {
        return meteoriteList.size
    }

    override fun getAllMeteorite(): List<Meteorite> {
        return meteoriteList
    }

    override fun deleteAllMeteorite() {
        meteoriteList.clear()
    }
    private fun log(msg:String){
        logD(msg)
    }
}