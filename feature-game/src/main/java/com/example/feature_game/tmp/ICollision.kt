//package com.example.feature_game.tmp
//
//import android.graphics.Bitmap
//import android.graphics.Rect
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import kotlinx.coroutines.*
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//import java.util.concurrent.Future
//import kotlin.math.absoluteValue
//
//interface ICollision {
//    fun getCurrentFrame(): Bitmap
//    fun getFrameHitBox(): Rect
//}
//
//@RequiresApi(Build.VERSION_CODES.Q)
//fun hitBoxDetection(imageOne: ICollision, imageTwo: ICollision): Boolean {
//    val rect = Rect(imageOne.getFrameHitBox())
//    val result = rect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
//    if (!result) {
//        return false
//    }
//    for (localY in (rect.top + 1) until rect.bottom) { //Y координата
//        for (localX in (rect.left + 1) until rect.right) {//X координата
//            val localXImgOne = (localX - (imageOne.getFrameHitBox().left)).absoluteValue
//            val localYImgOne = (localY - imageOne.getFrameHitBox().right).absoluteValue
//
//
//            val colorAlphaImageOne = try {
//                imageOne.getCurrentFrame().getColor(
//                    localXImgOne,
//                    localYImgOne
//                ).alpha()
//            } catch (ex: Throwable) {
//                Log.d(
//                    "TAG_6",
//                    "rect: $rect," +
//                            " imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
//                            " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}," +
//                            " localX:  $localX," +
//                            " localY: $localY," +
//                            " localXImgOne: $localXImgOne," +
//                            " localYImgOne: $localYImgOne," +
//                            " imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left.absoluteValue}," +
//                            " imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top.absoluteValue}," +
//                            " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                            " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                            "localX: $localX - imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left.absoluteValue} = localXImgOne: $localXImgOne," +
//                            "localY: $localY - imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top.absoluteValue} = localYImgOne: $localYImgOne," +
//                            "localX: $localX - imageTwo.getFrameHitBox().left: ${imageTwo.getFrameHitBox().left.absoluteValue} = localXImgTwo: ${(localX - imageTwo.getFrameHitBox().left.absoluteValue).absoluteValue}, " +
//                            "localX: $localY - imageTwo.getFrameHitBox().top: ${imageTwo.getFrameHitBox().top.absoluteValue} = localYImgTwo: ${(localY - imageTwo.getFrameHitBox().top.absoluteValue).absoluteValue}"
//                )
//                ex.printStackTrace()
//                0f
//            }
//
//            if (colorAlphaImageOne > 0) {
//                continue
//            }
//
//            val localXImgTwo = (localX - imageTwo.getFrameHitBox().left).absoluteValue
//            val localYImgTwo = (localY - imageTwo.getFrameHitBox().top).absoluteValue
//
//            val colorAlphaImageTwo = try {
//                imageTwo.getCurrentFrame().getColor(
//                    localXImgTwo,
//                    localYImgTwo
//                ).alpha()
//            } catch (ex: Throwable) {
//                Log.d(
//                    "TAG_6",
//                    "rect: $rect, imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
//                            " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}, localX:  $localX," +
//                            " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
//                            " imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left.absoluteValue}," +
//                            " imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top.absoluteValue}," +
//                            " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                            " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                            "localX: $localX - imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left.absoluteValue} = localXImgOne: $localXImgOne," +
//                            "localY: $localY - imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top.absoluteValue} = localYImgOne: $localYImgOne," +
//                            "localX: $localX - imageTwo.getFrameHitBox().left: ${imageTwo.getFrameHitBox().left.absoluteValue} = localXImgTwo: ${localX - imageTwo.getFrameHitBox().left.absoluteValue}, " +
//                            "localY: $localY - imageTwo.getFrameHitBox().top: ${imageTwo.getFrameHitBox().top.absoluteValue} = localYImgTwo: ${localY - imageTwo.getFrameHitBox().top.absoluteValue}"
//                )
//                ex.printStackTrace()
//                0f
//            }
//
//            if (colorAlphaImageTwo > 0) {
//                return true
//            }
//        }
//    }
//
//    return false
//}
//
//val handler = CoroutineExceptionHandler { _, exception ->
//    Log.e("TAG_6", "CoroutineExceptionHandler got $exception", exception)
//}
//
//val scope = CoroutineScope(Dispatchers.Default + handler + SupervisorJob())
//
//val threadFactory: ExecutorService = Executors.newCachedThreadPool()
//
//@RequiresApi(Build.VERSION_CODES.Q)
//fun hitBoxDetectionV2(
//    imageOne: ICollision,
//    imageTwo: ICollision,
//    result: (Boolean) -> Unit
//) {
//    scope.launch {
////        mutext.withLock {
//        val detectedCollisionRect = Rect(imageOne.getFrameHitBox())
//        val result =
//            detectedCollisionRect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
//        if (!result) {
//            result(false)
//        }
//        val subJobList = mutableListOf<Deferred<Boolean>>()
//        for (localY in (detectedCollisionRect.top + 1) until detectedCollisionRect.bottom) { //Y координата
//            val subJob: Deferred<Boolean> = scope.async {
//                if (isActive) {
//                    if (checkDetectCollisionInRow(
//                            detectedCollisionRect,
//                            imageOne,
//                            localY,
//                            imageTwo
//                        )
//                    ) {
//                        return@async true
//                    }
//                }
//                return@async false
//            }
//            if (!subJob.isCancelled) {
//                subJob.join()
//                subJobList.add(subJob)
//            }
//        }
//        subJobList.awaitAll().forEach {
//            if (it) result(false)
//        }
//        result(false)
//    }
////    }
//}
//
//@set: Synchronized
//@get: Synchronized
//var isActive = false
//
//@RequiresApi(Build.VERSION_CODES.Q)
//fun hitBoxDetectionV3(imageOne: ICollision, imageTwo: ICollision): Boolean {
//    val detectedCollisionRect = Rect(imageOne.getFrameHitBox())
//    val result =
//        detectedCollisionRect.setIntersect(imageOne.getFrameHitBox(), imageTwo.getFrameHitBox())
//    if (!result) {
//        return false
//    }
//    val must = mutableListOf<Future<Boolean>>()
//    isActive = false
//    for (localY in (detectedCollisionRect.top) until detectedCollisionRect.bottom) { //Y координата
//        val future: Future<Boolean> = threadFactory.submit<Boolean> {
//            return@submit checkDetectCollisionInRow2(
//                detectedCollisionRect,
//                imageOne,
//                localY,
//                imageTwo
//            )
//        }
//        must.add(future)
//    }
//    must.forEach {
//        if (it.get()) {
//            return true
//        }
//    }
//    return false
//}
//
//@RequiresApi(Build.VERSION_CODES.Q)
//private fun checkDetectCollisionInRow(
//    detectedCollisionRect: Rect,
//    imageOne: ICollision,
//    localY: Int,
//    imageTwo: ICollision
//): Boolean {
//    label@ for (localX in (detectedCollisionRect.left) until detectedCollisionRect.right) {//X координата
//        if (isActive) return false
//        val localXImgOne = (localX - (imageOne.getFrameHitBox().left)).absoluteValue
//        val localYImgOne = (localY - imageOne.getFrameHitBox().top).absoluteValue
//
//
//        val colorAlphaImageOne = try {
//            imageOne.getCurrentFrame().getColor(
//                localXImgOne,
//                localYImgOne
//            ).alpha()
//        } catch (ex: Throwable) {
//            Log.e(
//                "TAG_6",
//                "rect: $detectedCollisionRect," +
//                        " imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
//                        " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}," +
//                        " localX:  $localX," +
//                        " localY: $localY," +
//                        " localXImgOne: $localXImgOne," +
//                        " localYImgOne: $localYImgOne," +
//                        " imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left}," +
//                        " imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top}," +
//                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                        "localX: $localX - imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left} = localXImgOne: $localXImgOne," +
//                        "localY: $localY - imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top} = localYImgOne: $localYImgOne," +
//                        "localX: $localX - imageTwo.getFrameHitBox().left: ${imageTwo.getFrameHitBox().left} = localXImgTwo: ${(localX - imageTwo.getFrameHitBox().left).absoluteValue}, " +
//                        "localX: $localY - imageTwo.getFrameHitBox().top: ${imageTwo.getFrameHitBox().top} = localYImgTwo: ${(localY - imageTwo.getFrameHitBox().top).absoluteValue}",
//                ex
//            )
//            0f
//        }
//
//        if (colorAlphaImageOne == 0F) {
//            continue@label
//        }
//
//
//        val localXImgTwo = (localX - imageTwo.getFrameHitBox().left).absoluteValue
//        val localYImgTwo = (localY - imageTwo.getFrameHitBox().top).absoluteValue
//
//        val colorAlphaImageTwo = try {
//            imageTwo.getCurrentFrame().getColor(
//                localXImgTwo,
//                localYImgTwo
//            ).alpha()
//        } catch (ex: Throwable) {
//            Log.e(
//                "TAG_6",
//                "rect: $detectedCollisionRect, imageOne.getFrameHitBox(): ${imageOne.getFrameHitBox()}," +
//                        " imageTwo.getFrameHitBox(): ${imageTwo.getFrameHitBox()}, localX:  $localX," +
//                        " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
//                        " imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left}," +
//                        " imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top}," +
//                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                        "localX: $localX - imageOne.getFrameHitBox().left: ${imageOne.getFrameHitBox().left} = localXImgOne: $localXImgOne," +
//                        "localY: $localY - imageOne.getFrameHitBox().top: ${imageOne.getFrameHitBox().top} = localYImgOne: $localYImgOne," +
//                        "localX: $localX - imageTwo.getFrameHitBox().left: ${imageTwo.getFrameHitBox().left} = localXImgTwo: ${localX - imageTwo.getFrameHitBox().left}, " +
//                        "localY: $localY - imageTwo.getFrameHitBox().top: ${imageTwo.getFrameHitBox().top} = localYImgTwo: ${localY - imageTwo.getFrameHitBox().top}",
//                ex
//            )
//            0f
//        }
//
//        if (colorAlphaImageTwo > 0) {
//            Log.d(
//                "TAG_6",
//                "colorAlphaImageOne: $colorAlphaImageOne,localXImgOne: $localXImgOne, localYImgOne: $localYImgOne, colorAlphaImageTwo: $colorAlphaImageTwo, localXImgTwo: $localXImgTwo, localYImgTwo: $localYImgTwo"
//            )
//            isActive = true
//            return true
//        }
//    }
//    return false
//}
//
//@RequiresApi(Build.VERSION_CODES.Q)
//private fun checkDetectCollisionInRow2(
//    detectedCollisionRect: Rect,
//    imageOne: ICollision,
//    localY: Int,
//    imageTwo: ICollision
//): Boolean {
//    if (isActive) return false
//    val localDetectedCollisionRect = Rect(detectedCollisionRect)
//    val localImageOneDetectedCollisionRect = Rect(imageOne.getFrameHitBox())
//    val localImageTwoDetectedCollisionRect = Rect(imageTwo.getFrameHitBox())
//
//    label@ for (localX in (localDetectedCollisionRect.left) until localDetectedCollisionRect.right) {//X координата
//        if (isActive) return false
//        val localXImgOne = (localX - (localImageOneDetectedCollisionRect.left)).absoluteValue
//        val localYImgOne = (localY - localImageOneDetectedCollisionRect.top).absoluteValue
//
//        val colorAlphaImageOne = try {
//            imageOne.getCurrentFrame().getColor(
//                localXImgOne,
//                localYImgOne
//            ).alpha()
//        } catch (ex: Throwable) {
//            Log.e(
//                "TAG_6",
//                "rect: $localDetectedCollisionRect," +
//                        " localImageOneDetectedCollisionRect: ${localImageOneDetectedCollisionRect}," +
//                        " localImageTwoDetectedCollisionRect: ${localImageTwoDetectedCollisionRect}," +
//                        " localX:  $localX," +
//                        " localY: $localY," +
//                        " localXImgOne: $localXImgOne," +
//                        " localYImgOne: $localYImgOne," +
//                        " localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left}," +
//                        " localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top}," +
//                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                        "localX: $localX - localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left} = localXImgOne: $localXImgOne," +
//                        "localY: $localY - localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top} = localYImgOne: $localYImgOne," +
//                        "localX: $localX - localImageTwoDetectedCollisionRect.left: ${localImageTwoDetectedCollisionRect.left} = localXImgTwo: ${(localX - localImageTwoDetectedCollisionRect.left).absoluteValue}, " +
//                        "localX: $localY - localImageTwoDetectedCollisionRect.top: ${localImageTwoDetectedCollisionRect.top} = localYImgTwo: ${(localY - localImageTwoDetectedCollisionRect.top).absoluteValue}",
//                ex
//            )
//            0f
//        }
//
//        if (colorAlphaImageOne == 0F) {
//            continue@label
//        }
//
//        if (isActive) return false
//        val localXImgTwo = (localX - localImageTwoDetectedCollisionRect.left).absoluteValue
//        val localYImgTwo = (localY - localImageTwoDetectedCollisionRect.top).absoluteValue
//
//        val colorAlphaImageTwo = try {
//            imageTwo.getCurrentFrame().getColor(
//                localXImgTwo,
//                localYImgTwo
//            ).alpha()
//        } catch (ex: Throwable) {
//            Log.e(
//                "TAG_6",
//                "rect: $localDetectedCollisionRect, localImageOneDetectedCollisionRect: ${localImageOneDetectedCollisionRect}," +
//                        " localImageTwoDetectedCollisionRect: ${localImageTwoDetectedCollisionRect}, localX:  $localX," +
//                        " localY: $localY, localXImgOne: $localXImgOne, localYImgOne: $localYImgOne," +
//                        " localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left}," +
//                        " localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top}," +
//                        " imageOne frame width: ${imageOne.getCurrentFrame().width}," +
//                        " imageOne frame height: ${imageOne.getCurrentFrame().height}," +
//                        "localX: $localX - localImageOneDetectedCollisionRect.left: ${localImageOneDetectedCollisionRect.left} = localXImgOne: $localXImgOne," +
//                        "localY: $localY - localImageOneDetectedCollisionRect.top: ${localImageOneDetectedCollisionRect.top} = localYImgOne: $localYImgOne," +
//                        "localX: $localX - localImageTwoDetectedCollisionRect.left: ${localImageTwoDetectedCollisionRect.left} = localXImgTwo: ${localX - localImageTwoDetectedCollisionRect.left}, " +
//                        "localY: $localY - localImageTwoDetectedCollisionRect.top: ${localImageTwoDetectedCollisionRect.top} = localYImgTwo: ${localY - localImageTwoDetectedCollisionRect.top}",
//                ex
//            )
//            0f
//        }
//
//        if (colorAlphaImageTwo > 0) {
//            Log.d(
//                "TAG_6",
//                "colorAlphaImageOne: $colorAlphaImageOne,localXImgOne: $localXImgOne, localYImgOne: $localYImgOne, colorAlphaImageTwo: $colorAlphaImageTwo, localXImgTwo: $localXImgTwo, localYImgTwo: $localYImgTwo"
//            )
//            isActive = true
//            return true
//        }
//    }
//    return false
//}