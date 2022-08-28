package com.example.feature_game.tmp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.feature_game.repository.IMeteoriteRepository

class MeteoritesManager(
    private val meteoriteRepository: IMeteoriteRepository
) {
    private val paint: Paint = Paint()

    private val paint2 = Paint()
    init {
        paint.color = Color.WHITE
    }

    fun draw(canvas: Canvas) {
        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            try {
                val meteorite =
                    meteoriteRepository.getMeteoriteByIndex(i) //IndexOutOfBoundsException при рестарте игры (1 раз)
                canvas.drawBitmap(
                    meteorite.bitmap, //todo occasionally get IndexOfBounds Exception (3 times)
                    meteorite.x.toFloat(),
                    meteorite.y.toFloat(),
                    paint
                )
//                paint2.style = Paint.Style.STROKE
//                paint2.color = Color.RED
//
//                canvas.drawRect (
//                    meteorite.hitBox, //todo occasionally get IndexOfBounds Exception (3 times)
//                    paint2
//                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }
}