package rewheeldev.tappycosmicevasion.ui.customView

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository

class MeteoritesManager(
    private val meteoriteRepository: IMeteoriteRepository
) {
    private val paint: Paint = Paint()

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
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }
}