package ua.yandex.jere184.c4tappydefender.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.util.Public
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

    fun reInit() {
        val whichBitmap = Public.random.nextInt(6)
        size = (whichBitmap + 1).toByte()
        speed = 24 - size * 3
        bitmap = Public.scaleBitmap(animateImgArray[0], (size * 2).toByte())
        //bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.asteroid_01);
//    switch (whichBitmap) {
//      case 0:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_1);//
////        speed = 10;
////        size = 1;
//        break;
//      case 1:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_2);//
////        speed = 20;
////        size = 3;
//        break;
//      case 2:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_3);//
////        speed = 6;
//        //size = 1;
//        break;
//      case 3:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_4);//
////        speed = 18;
//        //size = 1;
//        break;
//      case 4:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);//enemy_5);//
////        speed = 13;
//        //size = 1;
//        break;
//      case 5:
//        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_01);
////        speed = 13;
//        //size = 3;
//        break;
//    }
        //bitmap = c_Public.f_scaleBitmap(bitmap, (byte) (size * 2));
        hitBox = Rect(x + 10, y + 10, bitmap!!.getWidth() - 10, bitmap!!.getHeight() - 10)
        y = Public.random.nextInt(maxY) - bitmap!!.getHeight()
        x = maxX + 100
    }

    fun update(playerSpeed: Float) {
        nextBitmapStep()
        x -= playerSpeed.toInt()
        x -= speed
        if (x < minX - bitmap!!.width) {
            reInit()
            //      speed = c_Public._random.nextInt(10) + 10;
//      x = maxX;
//      y = c_Public._random.nextInt(maxY) - bitmap.getHeight();
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
        if (bitmapIndex.toInt() == 127) //animateImgArray.size()-1)
            bitmapIndex = 0 else {
            if (bitmapIndex2 % 3 == 0) {
                bitmapIndex++
                bitmap = Public.scaleBitmap(
                    animateImgArray[bitmapIndex % animateImgArray.size] /*returned*/,
                    (size * 2).toByte()
                )
            }
        }
        //
//    byte pos_r = (byte) (bitmapIndex / 8);
//    byte pos_c = (byte) (bitmapIndex - (pos_r * 8));
//    int partImgSize = 192;
//
//    Bitmap returned /*bitmap*/ = Bitmap.createBitmap(_bigBitmap, pos_c * partImgSize, pos_r * partImgSize, partImgSize, partImgSize);
//    //bitmap= Bitmap.createBitmap(_bigBitmap, 128*0, 128*0, 128, 128);
    }

    fun setX(x: Int) {
        this.x = x
    }

    companion object {
        //region animations
        @JvmStatic
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