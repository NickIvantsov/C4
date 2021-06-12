package rewheeldev.tappycosmicevasion.repository

import android.graphics.Bitmap

interface IMeteoriteRepository {
    fun add(meteoriteImg: Bitmap)
    fun get(index:Int):Bitmap
    fun getSize(): Int
}