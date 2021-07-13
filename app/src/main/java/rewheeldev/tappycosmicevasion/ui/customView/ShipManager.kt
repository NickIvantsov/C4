package rewheeldev.tappycosmicevasion.ui.customView

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class ShipManager(
    private val spaceViewModel: SpaceViewModel
) {
    private val paint = Paint()
    init {
        paint.color = Color.argb(255, 255, 255, 255)
    }
    fun draw(canvas: Canvas) {
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
    }

}