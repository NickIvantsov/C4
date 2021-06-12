package rewheeldev.tappycosmicevasion.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import rewheeldev.tappycosmicevasion.R
import rewheeldev.tappycosmicevasion.util.Public
import java.util.*

class EnemyShip(private val maxX: Int, private val maxY: Int) {
    @JvmField
    var bitmap: Bitmap? = null

    @JvmField
    var x = 0

    @JvmField
    var y = 0
    private var speed = 1
    private var size: Byte = 2
    private val minX = 0
    private val minY = 0

    //endregion
    var hitBox: Rect? = null
        private set

    private fun reInit() {
        val whichBitmap = Public.random.nextInt(6)
        size = (whichBitmap + 1).toByte()
        speed = 24 - size * 3
        bitmap = Public.scaleBitmap(animateImgArray[0], (size * 2).toByte())
        hitBox = Rect(x + 10, y + 10, bitmap!!.width - 10, bitmap!!.height - 10)
        y = Public.random.nextInt(maxY) - bitmap!!.height
        x = maxX + 100
    }

    fun update(playerSpeed: Float) {
        nextBitmapStep()
        x -= playerSpeed.toInt()
        x -= speed
        if (x < minX - bitmap!!.width) {
            reInit()
        }

        //region прямоугольник столкновений
        hitBox!!.left = x + bitmap!!.width / 2 // = x; // тестовый вариант
        hitBox!!.top = y
        hitBox!!.right = x + bitmap!!.width
        hitBox!!.bottom = y + bitmap!!.height
        //endregion
    }

    private var bitmapIndex: Byte = 0
    private var bitmapIndex2: Byte = 0
    private fun nextBitmapStep() {
        bitmapIndex2++
        if (bitmapIndex.toInt() == 127)
            bitmapIndex = 0 else {
            if (bitmapIndex2 % 3 == 0) {
                bitmapIndex++
                bitmap = Public.scaleBitmap(
                    animateImgArray[bitmapIndex % animateImgArray.size],
                    (size * 2).toByte()
                )
            }
        }
    }

    fun setX(x: Int) {
        this.x = x
    }

    companion object {
        //region animations
        fun initBitmap() {
            bigBitmap =
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.asteroid_big)
            val partImgSizeX = bigBitmap!!.height / 8 // 192;
            for (r in 0..7) {
                for (c in 0..7) {
                    animateImgArray.add(
                        Bitmap.createBitmap(
                            bigBitmap!!,
                            c * partImgSizeX,
                            r * partImgSizeX,
                            partImgSizeX,
                            partImgSizeX
                        )
                    )
                }
            }
        }

        var animateImgArray = ArrayList<Bitmap>()
        var bigBitmap: Bitmap? = null
    }

    init {
        reInit()
    }
}