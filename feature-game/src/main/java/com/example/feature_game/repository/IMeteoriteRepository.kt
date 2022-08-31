package com.example.feature_game.repository

import android.graphics.Bitmap
import com.example.feature_game.model.Meteorite

interface IMeteoriteRepository {

    fun addMeteoriteBitmap(meteoriteImg: Bitmap)
    fun getMeteoriteBitmap(index: Int): Bitmap
    fun getSizeOfMeteoriteBitmaps(): Int

    fun getAllMeteoriteBitmaps(): List<Bitmap>

    fun addMeteorite(meteorite: Meteorite)
    fun getMeteoriteByIndex(index: Int): Meteorite
    fun getSizeMeteoriteList(): Int
    fun getAllMeteorite(): List<Meteorite>
    fun deleteAllMeteorite()
}