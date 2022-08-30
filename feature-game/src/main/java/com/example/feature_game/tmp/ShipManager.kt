package com.example.feature_game.tmp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class ShipManager(
    private val spaceViewModel: SpaceViewModel
) {
    private val paint = Paint()
    private val paint2 = Paint()

    init {
        paint.color = Color.argb(255, 255, 255, 255)
    }

    fun draw(
        canvas: Canvas,
        debugEnable: Boolean = false
    ) {
        val playerShipDrawInfo = spaceViewModel.getPlayerShipDrawInfo()
        canvas.drawBitmap(
            playerShipDrawInfo.ship,
            playerShipDrawInfo.shipX,
            playerShipDrawInfo.shipY,
            paint
        )

        if (spaceViewModel.player.isTouchSpeed) canvas.drawBitmap(
            playerShipDrawInfo.fire,
            playerShipDrawInfo.shipX + playerShipDrawInfo.fireX,
            playerShipDrawInfo.shipY + playerShipDrawInfo.fireY,
            paint
        )
        if (debugEnable) {
            paint2.style = Paint.Style.STROKE
            paint2.color = Color.RED
            canvas.drawRect(
                playerShipDrawInfo.shipHitBot,
                paint2
            )
        }
    }

}