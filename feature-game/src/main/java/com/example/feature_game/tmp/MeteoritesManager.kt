package com.example.feature_game.tmp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.feature_game.repository.IMeteoriteRepository
import java.util.concurrent.Executors

class MeteoritesManager(
    private val meteoriteRepository: IMeteoriteRepository,
    private val canvas: Canvas
) {
    private val paint: Paint = Paint()

    private val paint2 = Paint()

    init {
        paint.color = Color.WHITE
    }

    private val executor = Executors.newCachedThreadPool()
    fun draw() {
        val startTime = System.currentTimeMillis()
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

                paint2.style = Paint.Style.STROKE
                paint2.color = Color.RED

                canvas.drawCircle(
                    meteorite.x.toFloat() + ((meteorite.bitmap.width) / 2),
                    meteorite.y.toFloat() +  ((meteorite.bitmap.height ) / 2),
                    ((meteorite.bitmap.width - 100) / 2).toFloat(),
                    paint2
                )

                canvas.drawRect (
                    meteorite.hitBox,
                    paint2
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
//        Log.d("DRAW_1", "draw meteorites time cost: ${System.currentTimeMillis() - startTime}")
    }
}