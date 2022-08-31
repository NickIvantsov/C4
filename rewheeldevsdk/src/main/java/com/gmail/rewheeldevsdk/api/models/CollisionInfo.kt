package com.gmail.rewheeldevsdk.api.models

import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.absoluteValue

class CollisionInfo(
    val image: Bitmap,
    var detailing: Int = DETAILING_DEFAULT_VALUE
) {
    var collisionBlocks: Array<BooleanArray> = Array(0) {
        BooleanArray(0)
    }
        private set
        get() {
            if (field.isEmpty()) throw  Exception("")
            return field
        }


    @RequiresApi(Build.VERSION_CODES.Q)
    fun initializeImageBlocks() {
        val isDoubleWidth = image.width % detailing

        var collisionBlockWidth = image.width / detailing

        if (isDoubleWidth > 0) {
            collisionBlockWidth += 1
        }
        val isDoubleHeight = image.height % detailing

        var collisionBlockHeight = image.height / detailing

        if (isDoubleHeight > 0) {
            collisionBlockHeight += 1
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
                        if (x + indexX > image.width - 1) continue
                        val alpha = image.getColor(x + indexX, y + indexY).alpha()
                        if (alpha > 0) {
                            collisionBlocks[collisionBlockY][collisionBlockX] = true
                            break@blockLoop
                        }
                    }
                }
            }
        }
    }

    val threadFactory = Executors.newFixedThreadPool(1)
    fun checkCollision2(
        detectedCollisionRect: Rect,
        rectThisImage: Rect,
        rectSecondImage: Rect,
        secondImg: CollisionInfo,
        widthScaleThisImagePx: Int,
        widthScaleSecondImagePx: Int
    ): Boolean {
        val resultList = mutableListOf<Future<Boolean>>()
        for (y in detectedCollisionRect.top until detectedCollisionRect.bottom step detailing) {
            val result = threadFactory.submit<Boolean> {
                for (x in detectedCollisionRect.left until detectedCollisionRect.right step detailing) {

                    val thisImgX = (x - rectThisImage.left).absoluteValue
                    val thisImgY = (y - rectThisImage.top).absoluteValue
                    try {
                        if (checkCollisionByCoords(
                                thisImgX,
                                thisImgY,
                                widthScaleThisImagePx
                            ).not()
                        ) {
                            continue
                        }

                    } catch (ex: Throwable) {
                        Log.e(
                            "TAG_6",
                            "IMAGE ONE thisImgX: $thisImgX, thisImgY: $thisImgY | detectedCollisionRect: $detectedCollisionRect, rectThisImage: $rectThisImage, rectSecondImage: $rectSecondImage, secondImg: $secondImg, widthScaleThisImagePx: $widthScaleThisImagePx",
                            ex
                        )

                    }
                    val secondImgX = (x - rectSecondImage.left).absoluteValue
                    val secondImgY = (y - rectSecondImage.top).absoluteValue
                    try {

                        if (secondImg.checkCollisionByCoords(
                                secondImgX,
                                secondImgY,
                                widthScaleSecondImagePx
                            )
                        ) {
                            return@submit true
                        }
                    } catch (ex: Throwable) {
                        Log.e(
                            "TAG_6",
                            "IMAGE TWO secondImgX: $secondImgX, secondImgY: $secondImgY | detectedCollisionRect: $detectedCollisionRect, rectThisImage: $rectThisImage, rectSecondImage: $rectSecondImage, secondImg: $secondImg, widthScaleThisImagePx: $widthScaleThisImagePx",
                            ex
                        )
                    }
                }

                return@submit false
            }
            resultList.add(result)
        }
        resultList.forEach {
            if (it.get()) {
                return true
            }
        }
        return false
    }

    fun checkCollision(
        detectedCollisionRect: Rect,
        rectThisImage: Rect,
        rectSecondImage: Rect,
        secondImg: CollisionInfo,
        widthScaleThisImagePx: Int,
        widthScaleSecondImagePx: Int
    ): Boolean {
        for (y in detectedCollisionRect.top until detectedCollisionRect.bottom step detailing) {
            for (x in detectedCollisionRect.left until detectedCollisionRect.right step detailing) {

                val thisImgX = (x - rectThisImage.left).absoluteValue
                val thisImgY = (y - rectThisImage.top).absoluteValue
                try {
                    if (checkCollisionByCoords(
                            thisImgX,
                            thisImgY,
                            widthScaleThisImagePx
                        ).not()
                    ) {
                        continue
                    }

                } catch (ex: Throwable) {
                    Log.e(
                        "TAG_6",
                        "IMAGE ONE thisImgX: $thisImgX, thisImgY: $thisImgY | detectedCollisionRect: $detectedCollisionRect, rectThisImage: $rectThisImage, rectSecondImage: $rectSecondImage, secondImg: $secondImg, widthScaleThisImagePx: $widthScaleThisImagePx",
                        ex
                    )

                }
                val secondImgX = (x - rectSecondImage.left).absoluteValue
                val secondImgY = (y - rectSecondImage.top).absoluteValue
                try {

                    if (secondImg.checkCollisionByCoords(
                            secondImgX,
                            secondImgY,
                            widthScaleSecondImagePx
                        )
                    ) {
                        return true
                    }
                } catch (ex: Throwable) {
                    Log.e(
                        "TAG_6",
                        "IMAGE TWO secondImgX: $secondImgX, secondImgY: $secondImgY | detectedCollisionRect: $detectedCollisionRect, rectThisImage: $rectThisImage, rectSecondImage: $rectSecondImage, secondImg: $secondImg, widthScaleThisImagePx: $widthScaleThisImagePx",
                        ex
                    )
                }
            }
        }
        return false
    }

    fun checkCollisionByCoords(x: Int, y: Int, widthScaleImagePx: Int): Boolean {

        val scale = calcScale(widthScaleImagePx)

        val yCalc = y / (detailing * scale)
        val xCalc = x / (detailing * scale)
        return collisionBlocks[yCalc.toInt()][xCalc.toInt()]
    }

    private fun calcScale(newImgWidthPx: Int): Float = newImgWidthPx.toFloat() / image.width

    companion object {
        const val DETAILING_DEFAULT_VALUE = 2
    }
}

