package com.example.feature_game.repository

import android.graphics.Bitmap
import com.example.core_utils.util.logging.extensions.logD
import com.example.feature_game.model.Meteorite
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

    override fun getAllMeteoriteBitmaps(): List<Bitmap> {
        return animateImgArray
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

    private fun log(msg: String) {
        logD(msg)
    }
}