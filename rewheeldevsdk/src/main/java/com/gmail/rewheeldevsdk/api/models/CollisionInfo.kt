package com.gmail.rewheeldevsdk.api.models

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi

class CollisionInfo(
    val image: Bitmap,
    val detailing: Int = DETAILING_DEFAULT_VALUE
) {
    var collisionBlocks: Array<BooleanArray> = Array(0) {
        BooleanArray(0)
    }
        private set
        get() {
            if (collisionBlocks.isEmpty()) throw  Exception("")
            return field
        }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun initializeImageBlocks() {
        val isDoubleWidth = image.width % detailing

        var collisionBlockWidth = image.width / detailing

        if (isDoubleWidth > 0) {
            collisionBlockWidth = ++collisionBlockWidth
        }
        val isDoubleHeight = image.height % detailing

        var collisionBlockHeight = image.height / detailing

        if (isDoubleHeight > 0) {
            collisionBlockHeight = ++collisionBlockHeight
        }
        collisionBlocks = Array(collisionBlockHeight) {
            BooleanArray(collisionBlockWidth)
        }

        for (collisionBlockY in 0 until collisionBlockHeight) {
            for (collisionBlockX in 0 until collisionBlockWidth) {
                val indexY = collisionBlockY * detailing
                val indexX = collisionBlockX * detailing
                blockLoop@ for (y in 0..detailing) {
                    if (y + indexY > image.height - 1) continue
                    for (x in 0..detailing) {
                        if (x + indexX > image.height - 1) continue
                        val alpha = image.getColor(x + indexX, y + indexY).alpha()
                        if (alpha > 0) {
                            collisionBlocks[collisionBlockY][collisionBlockX] = true
                            continue@blockLoop
                        }
                    }
                }
            }
        }
    }


    companion object {
        const val DETAILING_DEFAULT_VALUE = 1
    }
}

