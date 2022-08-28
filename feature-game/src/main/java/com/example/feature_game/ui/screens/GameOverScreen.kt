package com.example.feature_game.ui.screens

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.feature_game.R
import com.example.feature_game.tmp.SpaceView

class GameOverScreen(private val context: Context, private val canvas: Canvas) {
    private val paint = Paint()
    fun showGameOverScreen(screenX: Int) {
        paint.textSize = 80f
        paint.textAlign = Paint.Align.CENTER
        val gameOverStr = getResString(R.string.game_ower)

        canvas.drawText(gameOverStr, (screenX / 2).toFloat(), 100f, paint)

        paint.textSize = 25f
        //canvas.drawText("Level" + fastestTime + "s", screenX / 2, 160, paint);
        val timeStr = getResString(R.string.time) + SpaceView.timeTaken / 1000 + "s"
        canvas.drawText(
            timeStr,
            (screenX / 2).toFloat(),
            200f,
            paint
        )
        val result =
            "${getResString(R.string.distance_remaining_msg)}  ${SpaceView.distance.toInt()} ${
                getResString(R.string.km)
            }"
        canvas.drawText(
            result,
            (screenX / 2).toFloat(),
            240f,
            paint
        )
        paint.textSize = 80f

        canvas.drawText(
            context.getString(R.string.tap_to_replay_msg),
            (screenX / 2).toFloat(),
            350f,
            paint
        )
    }

    private fun getResString(id: Int): String {
        return context.applicationContext.getString(id)
    }
}