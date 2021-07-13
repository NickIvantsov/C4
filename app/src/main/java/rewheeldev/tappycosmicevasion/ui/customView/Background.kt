package rewheeldev.tappycosmicevasion.ui.customView

import android.graphics.Canvas
import android.graphics.Color

class Background {

    private val backgroundColor = Color.argb(255, 0, 0, 0)
    fun draw(canvas: Canvas) {
        canvas.drawColor(backgroundColor)
    }
}