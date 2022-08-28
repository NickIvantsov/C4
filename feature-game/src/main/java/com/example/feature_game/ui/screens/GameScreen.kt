package com.example.feature_game.ui.screens

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.feature_game.tmp.SpaceView

class GameScreen(private val canvas: Canvas) {
    private val paint = Paint()

    fun showGameScreen(
        screenX: Int,
        screenY: Int,
        speed: Short,
        isReduceShieldStrength: Byte,
        level: Int,
        lives: Int,
        isTouch: Boolean,
        changReduceShieldStrength: () -> Unit = DEFFAULT,
        drawSpaceDust: () -> Unit = DEFFAULT,
        drawShip: () -> Unit = DEFFAULT,
        drawMeteorites: () -> Unit = DEFFAULT,
        drawJoystick: () -> Unit = DEFFAULT
    ) {
        drawSpaceDust()
        drawShip()
        drawMeteorites()

        if (isTouch) {
            drawJoystick()
        }

        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.argb(255, 255, 255, 255)
        paint.textSize = 25f
        canvas.drawText("Level: $level", 20f, 30f, paint)
        canvas.drawText(
            "Time:" + SpaceView.timeTaken / 1000 + "s",
            (screenX / 2).toFloat(),
            20f,
            paint
        )
        canvas.drawText(
            "Distance:" + SpaceView.distance.toInt().toShort() + "KM",
            (screenX / 3).toFloat(),
            (screenY - 20).toFloat(),
            paint
        )
        canvas.drawText(
            "Speed: $speed KMh",
            (screenX / 3 * 2).toFloat(),
            (screenY - 20).toFloat(),
            paint
        )
        if (isReduceShieldStrength <= 0) {
            paint.textSize = 25f
        } else {
            if (isReduceShieldStrength % 10 > 5) {
                paint.color = Color.argb(255, 255, 0, 0)
            } else {
                paint.color = Color.argb(255, 255, 255, 255)
            }
            paint.textSize = (25 + isReduceShieldStrength).toFloat()
            changReduceShieldStrength()
        }
        canvas.drawText(
            "Shield: $lives",
            10f,
            (screenY - 20).toFloat(),
            paint
        )
    }

    companion object {
        private val DEFFAULT = {}
    }
}