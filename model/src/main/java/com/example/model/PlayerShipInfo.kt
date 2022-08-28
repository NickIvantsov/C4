package com.example.model

import android.graphics.Bitmap
import android.graphics.Rect

data class PlayerShipDrawInfo(
    val ship: Bitmap,
    val shipX: Float,
    val shipY: Float,
    val fire:Bitmap,
    val fireX:Float,
    val fireY: Float,
    val shipHitBot: Rect
    )