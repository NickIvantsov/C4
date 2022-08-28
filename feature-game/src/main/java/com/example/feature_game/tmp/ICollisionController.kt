package com.example.feature_game.tmp

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.absoluteValue

interface ICollisionController {
    fun getCurrentFrame(): Bitmap
    fun getCoordinates(): Point
    fun getFrameHitBox(): Rect
}

@RequiresApi(Build.VERSION_CODES.Q)
fun hitBoxDetection(imageOne: ICollisionController, imageTwo: ICollisionController): Boolean {
    val rect = Rect(imageOne.getFrameHitBox())
    val result = rect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
    if (!result) {
        return false
    }
    for (localY in (rect.top + 1) until rect.bottom) { //Y координата
        for (localX in (rect.left + 1) until rect.right) {//X координата
            val localXImgOne = (localX - (imageOne.getCoordinates().x).absoluteValue).absoluteValue
            val localYImgOne = (localY - imageOne.getCoordinates().y.absoluteValue).absoluteValue


            val colorAlphaImageOne = try {
                imageOne.getCurrentFrame().getColor(
                    localXImgOne,
                    localYImgOne
                ).alpha()
            } catch (ex: Throwable) {
                Log.d(
                    "TAG_6",
                    "rect: $rect," +
                            " imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
                            " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}," +
                            " localX:  $localX," +
                            " localY: $localY," +
                            " localXImgOne: $localXImgOne," +
                            " localYImgOne: $localYImgOne," +
                            " imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue}," +
                            " imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue}," +
                            " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                            " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                            "localX: $localX - imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue} = localXImgOne: $localXImgOne," +
                            "localY: $localY - imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue} = localYImgOne: $localYImgOne," +
                            "localX: $localX - imageTwo.getCoordinates().x: ${imageTwo.getCoordinates().x.absoluteValue} = localXImgTwo: ${(localX - imageTwo.getCoordinates().x.absoluteValue).absoluteValue}, " +
                            "localX: $localY - imageTwo.getCoordinates().y: ${imageTwo.getCoordinates().y.absoluteValue} = localYImgTwo: ${(localY - imageTwo.getCoordinates().y.absoluteValue).absoluteValue}"
                )
                ex.printStackTrace()
                0f
            }

            if (colorAlphaImageOne > 0) {
                continue
            }

            val localXImgTwo = (localX - imageTwo.getCoordinates().x.absoluteValue).absoluteValue
            val localYImgTwo = (localY - imageTwo.getCoordinates().y.absoluteValue).absoluteValue

            val colorAlphaImageTwo = try {
                imageTwo.getCurrentFrame().getColor(
                    localXImgTwo,
                    localYImgTwo
                ).alpha()
            } catch (ex: Throwable) {
                Log.d(
                    "TAG_6",
                    "rect: $rect, imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
                            " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}, localX:  $localX," +
                            " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
                            " imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue}," +
                            " imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue}," +
                            " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                            " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                            "localX: $localX - imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue} = localXImgOne: $localXImgOne," +
                            "localY: $localY - imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue} = localYImgOne: $localYImgOne," +
                            "localX: $localX - imageTwo.getCoordinates().x: ${imageTwo.getCoordinates().x.absoluteValue} = localXImgTwo: ${localX - imageTwo.getCoordinates().x.absoluteValue}, " +
                            "localY: $localY - imageTwo.getCoordinates().y: ${imageTwo.getCoordinates().y.absoluteValue} = localYImgTwo: ${localY - imageTwo.getCoordinates().y.absoluteValue}"
                )
                ex.printStackTrace()
                0f
            }

            if (colorAlphaImageTwo > 0) {
                return true
            }
        }
    }

    return false
}

val handler = CoroutineExceptionHandler { _, exception ->
    Log.e("TAG_6", "CoroutineExceptionHandler got $exception", exception)
}

val scope = CoroutineScope(Dispatchers.Default + handler + SupervisorJob())
val mutext = Mutex()

val threadFactory = Executors.newFixedThreadPool(10)

@RequiresApi(Build.VERSION_CODES.Q)
fun hitBoxDetectionV2(
    imageOne: ICollisionController,
    imageTwo: ICollisionController,
    result: (Boolean) -> Unit
) {
    scope.launch {
//        mutext.withLock {
        val rect = Rect(imageOne.getFrameHitBox())
        val result = rect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
        if (!result) {
            result(false)
        }
        val subJobList = mutableListOf<Deferred<Boolean>>()
        for (localY in (rect.top + 1) until rect.bottom) { //Y координата
            val subJob: Deferred<Boolean> = scope.async {
                if (isActive) {
                    if (checkDetectCollisionInRow(rect, imageOne, localY, imageTwo)) {
//                        scope.coroutineContext.cancelChildren()
                        return@async true
                    }
                }
                return@async false
            }
            if (!subJob.isCancelled) {
                subJob.join()
                subJobList.add(subJob)
            }
        }

//        subJobList.forEach {
//            if (it.await()) return true
//        }
        subJobList.awaitAll().forEach {
            if (it) result(false)
        }
        result(false)
    }
//    }
}

@set: Synchronized
@get: Synchronized
var isActive = false

@RequiresApi(Build.VERSION_CODES.Q)
fun hitBoxDetectionV3(imageOne: ICollisionController, imageTwo: ICollisionController): Boolean {
    val rect = Rect(imageOne.getFrameHitBox())
    val result = rect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
    if (!result) {
        return false
    }
    val must = mutableListOf<Future<Boolean>>()
    isActive = false
    for (localY in (rect.top + 1) until rect.bottom) { //Y координата
        val future: Future<Boolean> = threadFactory.submit<Boolean> {
            return@submit checkDetectCollisionInRow(rect, imageOne, localY, imageTwo)
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
    rect: Rect,
    imageOne: ICollisionController,
    localY: Int,
    imageTwo: ICollisionController
): Boolean {
    for (localX in (rect.left + 1) until rect.right) {//X координата
        if (isActive) return false
        val localXImgOne = (localX - (imageOne.getCoordinates().x).absoluteValue).absoluteValue
        val localYImgOne = (localY - imageOne.getCoordinates().y.absoluteValue).absoluteValue


        val colorAlphaImageOne = try {
            imageOne.getCurrentFrame().getColor(
                localXImgOne,
                localYImgOne
            ).alpha()
        } catch (ex: Throwable) {
            Log.d(
                "TAG_6",
                "rect: $rect," +
                        " imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
                        " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}," +
                        " localX:  $localX," +
                        " localY: $localY," +
                        " localXImgOne: $localXImgOne," +
                        " localYImgOne: $localYImgOne," +
                        " imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue}," +
                        " imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue}," +
                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                        "localX: $localX - imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue} = localXImgOne: $localXImgOne," +
                        "localY: $localY - imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue} = localYImgOne: $localYImgOne," +
                        "localX: $localX - imageTwo.getCoordinates().x: ${imageTwo.getCoordinates().x.absoluteValue} = localXImgTwo: ${(localX - imageTwo.getCoordinates().x.absoluteValue).absoluteValue}, " +
                        "localX: $localY - imageTwo.getCoordinates().y: ${imageTwo.getCoordinates().y.absoluteValue} = localYImgTwo: ${(localY - imageTwo.getCoordinates().y.absoluteValue).absoluteValue}"
            )
            ex.printStackTrace()
            0f
        }

        if (colorAlphaImageOne > 0) {
            continue
        }

        val localXImgTwo = (localX - imageTwo.getCoordinates().x.absoluteValue).absoluteValue
        val localYImgTwo = (localY - imageTwo.getCoordinates().y.absoluteValue).absoluteValue

        val colorAlphaImageTwo = try {
            imageTwo.getCurrentFrame().getColor(
                localXImgTwo,
                localYImgTwo
            ).alpha()
        } catch (ex: Throwable) {
            Log.d(
                "TAG_6",
                "rect: $rect, imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
                        " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}, localX:  $localX," +
                        " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
                        " imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue}," +
                        " imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue}," +
                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
                        "localX: $localX - imageOne.getCoordinates().x: ${imageOne.getCoordinates().x.absoluteValue} = localXImgOne: $localXImgOne," +
                        "localY: $localY - imageOne.getCoordinates().y: ${imageOne.getCoordinates().y.absoluteValue} = localYImgOne: $localYImgOne," +
                        "localX: $localX - imageTwo.getCoordinates().x: ${imageTwo.getCoordinates().x.absoluteValue} = localXImgTwo: ${localX - imageTwo.getCoordinates().x.absoluteValue}, " +
                        "localY: $localY - imageTwo.getCoordinates().y: ${imageTwo.getCoordinates().y.absoluteValue} = localYImgTwo: ${localY - imageTwo.getCoordinates().y.absoluteValue}"
            )
            ex.printStackTrace()
            0f
        }

        if (colorAlphaImageTwo > 0) {
            isActive = true
            return true
        }
    }
    return false
}