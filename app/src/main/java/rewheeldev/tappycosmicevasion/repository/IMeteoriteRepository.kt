package rewheeldev.tappycosmicevasion.repository

import android.graphics.Bitmap
import rewheeldev.tappycosmicevasion.model.Meteorite

interface IMeteoriteRepository {

    fun addMeteoriteBitmap(meteoriteImg: Bitmap)
    fun getMeteoriteBitmap(index: Int): Bitmap
    fun getSizeOfMeteoriteBitmaps(): Int

    fun addMeteorite(meteorite: Meteorite)
    fun getMeteoriteByIndex(index: Int): Meteorite
    fun getSizeMeteoriteList(): Int
    fun getAllMeteorite(): List<Meteorite>
    fun deleteAllMeteorite()
}