package rewheeldev.tappycosmicevasion.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import rewheeldev.tappycosmicevasion.R
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import rewheeldev.tappycosmicevasion.util.scaleBitmap
import java.util.*

class Meteorite(
    private val maxX: Int,
    private val maxY: Int,
    private val random: Random,
    private val screenSize: Point,
    private val meteoriteRepository: IMeteoriteRepository
) {
    lateinit var bitmap: Bitmap
    var x = 0
    var y = 0
    private var speed = 1
    private var size: Int = 2
    private val minX = 0
    private val minY = 0

    //endregion
    lateinit var hitBox: Rect
        private set

    private fun reInit() {
        val whichBitmap = random.nextInt(6)
        size = whichBitmap + 1
        speed = 24 - size * 3
        bitmap = scaleBitmap(meteoriteRepository.getMeteoriteBitmap(0), size * 2, screenSize.x)
        hitBox = Rect(x + 10, y + 10, bitmap.width - 10, bitmap.height - 10)
        y = random.nextInt(maxY) - bitmap.height
        x = maxX + 100
    }

    fun update(playerSpeed: Float) {
        nextBitmapStep()
        x -= playerSpeed.toInt()
        x -= speed
        if (x < minX - bitmap.width) {
            reInit()
        }

        //region прямоугольник столкновений
        hitBox.left = x + bitmap.width / 2 // = x; // тестовый вариант
        hitBox.top = y
        hitBox.right = x + bitmap.width
        hitBox.bottom = y + bitmap.height
        //endregion
    }

    private var bitmapIndex = 0
    private var bitmapIndex2 = 0
    private fun nextBitmapStep() {
        bitmapIndex2++
        if (bitmapIndex == 127)
            bitmapIndex = 0 else {
            if (bitmapIndex2 % 3 == 0) {
                bitmapIndex++
                val index = bitmapIndex % meteoriteRepository.getSizeOfMeteoriteBitmaps()
                bitmap = scaleBitmap(
                    meteoriteRepository.getMeteoriteBitmap(index),
                    size * 2,
                    screenSize.x
                )
            }
        }
    }

    companion object {
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
}