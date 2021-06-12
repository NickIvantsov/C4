package rewheeldev.tappycosmicevasion.util

import android.graphics.Bitmap
import android.graphics.Point

//уменьшаем изображение по размеру
fun scaleBitmap(inBitmap: Bitmap, multiplier: Byte, screenSize: Point): Bitmap {
    val newHeight = (screenSize.x / 70 * multiplier).toFloat()
    val oldHeight = inBitmap.height.toFloat()
    val oldWidth = inBitmap.width.toFloat()
    val r1 = (newHeight - oldHeight) / (oldHeight / 100)
    val r2 = oldWidth + oldWidth / 100 * r1
    return Bitmap.createScaledBitmap(inBitmap, r2.toInt(), newHeight.toInt(), false)
}