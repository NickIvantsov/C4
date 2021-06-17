package rewheeldev.tappycosmicevasion.model

import android.graphics.Bitmap

data class PlayerShipDrawInfo(
    val ship: Bitmap,
    val shipX: Float,
    val shipY: Float,
    val fire:Bitmap,
    val fireX:Float,
    val fireY: Float
    )