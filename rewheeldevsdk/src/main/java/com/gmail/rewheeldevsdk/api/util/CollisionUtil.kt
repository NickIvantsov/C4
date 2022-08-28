package com.gmail.rewheeldevsdk.api.util

import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.gmail.rewheeldevsdk.api.collision.ICollision
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.absoluteValue

@set: Synchronized
@get: Synchronized
private var isActive = false

val threadFactory: ExecutorService = Executors.newCachedThreadPool()

@RequiresApi(Build.VERSION_CODES.Q)
fun hitBoxDetection(imageOne: ICollision, imageTwo: ICollision): Boolean {
    val detectedCollisionRect = Rect(imageOne.getFrameHitBox())
    val result =
        detectedCollisionRect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
    if (!result) {
        return false
    }
    val must = mutableListOf<Future<Boolean>>()
    isActive = false
    for (localY in (detectedCollisionRect.top) until detectedCollisionRect.bottom) { //Y координата
        val future: Future<Boolean> = threadFactory.submit<Boolean> {
            return@submit checkDetectCollisionInRow(
                detectedCollisionRect,
                imageOne,
                localY,
                imageTwo
            )
        }
        must.add(future)
    }
    must.forEach {
        if (it.get()) {
            return true
        }
    }
    return false
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun checkDetectCollisionInRow(
    detectedCollisionRect: Rect,
    imageOne: ICollision,
    localY: Int,
    imageTwo: ICollision
): Boolean {
    if (isActive) return false
    val localDetectedCollisionRect = Rect(detectedCollisionRect)
    val localImageOneDetectedCollisionRect = Rect(imageOne.getFrameHitBox())
    val localImageTwoDetectedCollisionRect = Rect(imageTwo.getFrameHitBox())

    label@ for (localX in (localDetectedCollisionRect.left) until localDetectedCollisionRect.right) {//X координата
        if (isActive) return false
        val localXImgOne = (localX - (localImageOneDetectedCollisionRect.left)).absoluteValue
        val localYImgOne = (localY - localImageOneDetectedCollisionRect.top).absoluteValue

        val colorAlphaImageOne = try {
            imageOne.getCurrentFrame().getColor(
                localXImgOne,
                localYImgOne
            ).alpha()
        } catch (ex: Throwable) {
            Log.e(
                "TAG_6",
                "rect: $localDetectedCollisionRect," +
                        " localImageOneDetectedCollisionRect: ${localImageOneDetectedCollisionRect}," +
                        " localImageTwoDetectedCollisionRect: ${localImageTwoDetectedCollisionRect}," +
                        " localX:  $localX," +
                        " localY: $localY," +
                        " localXImgOne: $localXImgOne," +
                        " localYImgOne: $localYImgOne," +
                        " localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left}," +
                        " localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top}," +
                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                        "localX: $localX - localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left} = localXImgOne: $localXImgOne," +
                        "localY: $localY - localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top} = localYImgOne: $localYImgOne," +
                        "localX: $localX - localImageTwoDetectedCollisionRect.left: ${localImageTwoDetectedCollisionRect.left} = localXImgTwo: ${(localX - localImageTwoDetectedCollisionRect.left).absoluteValue}, " +
                        "localX: $localY - localImageTwoDetectedCollisionRect.top: ${localImageTwoDetectedCollisionRect.top} = localYImgTwo: ${(localY - localImageTwoDetectedCollisionRect.top).absoluteValue}",
                ex
            )
            0f
        }

        if (colorAlphaImageOne == 0F) {
            continue@label
        }

        if (isActive) return false
        val localXImgTwo = (localX - localImageTwoDetectedCollisionRect.left).absoluteValue
        val localYImgTwo = (localY - localImageTwoDetectedCollisionRect.top).absoluteValue

        val colorAlphaImageTwo = try {
            imageTwo.getCurrentFrame().getColor(
                localXImgTwo,
                localYImgTwo
            ).alpha()
        } catch (ex: Throwable) {
            Log.e(
                "TAG_6",
                "rect: $localDetectedCollisionRect, localImageOneDetectedCollisionRect: ${localImageOneDetectedCollisionRect}," +
                        " localImageTwoDetectedCollisionRect: ${localImageTwoDetectedCollisionRect}, localX:  $localX," +
                        " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
                        " localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left}," +
                        " localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top}," +
                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                        "localX: $localX - localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left} = localXImgOne: $localXImgOne," +
                        "localY: $localY - localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top} = localYImgOne: $localYImgOne," +
                        "localX: $localX - localImageTwoDetectedCollisionRect.left: ${localImageTwoDetectedCollisionRect.left} = localXImgTwo: ${localX - localImageTwoDetectedCollisionRect.left}, " +
                        "localY: $localY - localImageTwoDetectedCollisionRect.top: ${localImageTwoDetectedCollisionRect.top} = localYImgTwo: ${localY - localImageTwoDetectedCollisionRect.top}",
                ex
            )
            0f
        }

        if (colorAlphaImageTwo > 0) {
            Log.d(
                "TAG_6",
                "colorAlphaImageOne: $colorAlphaImageOne,localXImgOne: $localXImgOne, localYImgOne: $localYImgOne, colorAlphaImageTwo: $colorAlphaImageTwo, localXImgTwo: $localXImgTwo, localYImgTwo: $localYImgTwo"
            )
            isActive = true
            return true
        }
    }
    return false
}