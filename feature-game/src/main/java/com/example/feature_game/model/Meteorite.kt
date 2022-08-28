package com.example.feature_game.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import com.example.core_utils.util.scaleBitmap
import com.example.feature_game.R
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.feature_game.tmp.ICollisionController
import java.util.*


class Meteorite(
    private val maxX: Int,
    private val maxY: Int,
    private val random: Random,
    private val screenSize: Point,
    private val meteoriteRepository: IMeteoriteRepository
) : ICollisionController {
    lateinit var bitmap: Bitmap
    var x = 0
    var y = 0
    private var speed = 1
    private var size: Int = 2
    private val minX = 0
    private val minY = 0

    //endregion
    var hitBox: Rect = Rect()
        private set

    private fun reInit() {
        // здесь мы перемещаем метеорит на самое начело за пределы экрана по оси Х и меняем ему размер
//        при этом учитывается случайное смещение по оси У

        size =
            random.nextInt(6) + 1 //TODO: посотреть ограничения для метода рандома от 1 до 7 без учета 0
        //скорость метеорита зависит от его размера который мы получили в size переменной (чем меньше метеорит тем быстрее он летит)
        speed = 24 - size * 3
        bitmap = scaleBitmap(
            meteoriteRepository.getMeteoriteBitmap(0),
            size * 2,
            screenSize.x
        )

        y = random.nextInt(maxY) - bitmap.height
        x = maxX + 100

        setHitBox()
    }

    private fun setHitBox() {
        hitBox.left = x
        hitBox.top = y
        hitBox.right = x + bitmap.width
        hitBox.bottom = y + bitmap.height
    }

    fun update(playerSpeed: Float) {
        nextBitmapStep()
        x -= playerSpeed.toInt()
        x -= speed
        if (x < minX - bitmap.width) {
            reInit()
        }

        setHitBox()
    }

    private var bitmapIndex = 0

    //счетчик входа в nextBitmapStep
    private var frameCount = 0

    private fun nextBitmapStep() {
        frameCount++
        if (frameCount == Int.MAX_VALUE) {
            frameCount = 0
        }
        val meteoriteRepositorySize = meteoriteRepository.getSizeOfMeteoriteBitmaps()
        if (bitmapIndex == (meteoriteRepositorySize * 2) - 1) {
            bitmapIndex = 0
        }
        //выполняем переключение на следующую картинку какждый третий вход в функцию
        //для уменьшения скорости вращения метеорита
        if (frameCount % 3 == 0) { //TODO: 3 это надо заменить на вариативное значение которое будет просчитыватся в зависимости от производительности (FPS)
            bitmapIndex++
            //получение изображения для отрисовки
            val index = bitmapIndex % meteoriteRepositorySize
            val nextBitmap = meteoriteRepository.getMeteoriteBitmap(index)
            bitmap = scaleBitmap(
                nextBitmap,
                size * 2,
                screenSize.x
            )
        }
    }

    private fun printDebugLog(msg: String) {
        Log.d(TAG, "Thread name: ${Thread.currentThread().name}, msg: $msg")
    }

    companion object {
        private const val TAG = "TAG_2"
        //размер массива умноженый на 2 -1
        //число будет покрывать общее количество всех фреймов который у нас есть в масиве для отрисовки

        //region animations
        fun initBitmap(
            context: Context,
            meteoriteRepository: IMeteoriteRepository
        ) {
            val bigBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.asteroid_big)
            val partImgSizeX = bigBitmap.height / 8 // 192;
            for (r in 0..7) {
                for (c in 0..7) {
                    meteoriteRepository.addMeteoriteBitmap(
                        Bitmap.createBitmap(
                            bigBitmap,
                            c * partImgSizeX,
                            r * partImgSizeX,
                            partImgSizeX,
                            partImgSizeX
                        )
                    )
                }
            }
        }

        fun createNewMeteorite(
            maxX: Int,
            maxY: Int,
            random: Random,
            screenSize: Point,
            meteoriteRepository: IMeteoriteRepository
        ): Meteorite {
            return Meteorite(maxX, maxY, random, screenSize, meteoriteRepository)
        }
    }

    init {
        reInit()
    }

    override fun getCurrentFrame(): Bitmap = bitmap

    override fun getCoordinates(): Point = Point(x, y)
    override fun getFrameHitBox(): Rect = hitBox
}