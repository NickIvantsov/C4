package ua.yandex.jere184.c4tappydefender.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.util.Public

class PlayerShip {
    private var shipImg //_shipImg1,_shipImg2,_shipImg3;
            : Bitmap? = null
    @JvmField
    var fireImg: Bitmap? = null
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f
    @JvmField
    var fireX = 0f
    @JvmField
    var fireY = 0f
    @JvmField
    var touchY = 50f
    private var speed = 0f
    private var GRAVITY = 25
    private var maxY = 0
    private var minY = 0
    private val MIN_SPEED = 2
    private var MAX_SPEED = 45
    var hitBox: Rect? = null
        private set
    var lifes = 0
        private set

    //public  List<Bitmap> _shipImgList = new ArrayList<Bitmap>();
    //region flags
    @JvmField
    var isReduceShieldStrength: Byte = 0
    @JvmField
    var isTouchSpeed = false
    var isTouchMove = false
    var isTouchUP = false

    //endregion
    fun update() {
        nextBitmapStep()
        if (isTouchSpeed) speed(speed + 0.4.toFloat()) else speed(speed - 0.6.toFloat())

        //y = touchY;//не плавное ))
        //region плавное перемещение к пальцу по У
        val dist = touchY - y
        if (dist > GRAVITY || dist < 0 - GRAVITY) {
            if (dist > 0) y += GRAVITY.toFloat() else y -= GRAVITY.toFloat()
        } else y += dist
        //endregion

        //region проверка макс и мин
        if (y < minY) {
            y = minY.toFloat()
        }
        if (y > maxY) {
            y = maxY.toFloat()
        }
        //endregion
        //region прямоугольник столкновений
        hitBox!!.left = x.toInt()
        hitBox!!.top = y.toInt()
        hitBox!!.right = x.toInt() + shipImg!!.width
        hitBox!!.bottom = y.toInt() + shipImg!!.height
        //endregion
    }

    fun minusLifes() {
        isReduceShieldStrength = 25
        lifes--
    }

    private fun reinitLifes() {
        lifes = 10
    }

    fun reInit() {
        x = 70f //!!! 50;
        y = 250f
        speed(1f)
        when (Public.playerShipType) {
            0.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship_1)
                MAX_SPEED = 60
                GRAVITY = 10
            }
            1.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship)
                MAX_SPEED = 30
                GRAVITY = 40
            }
            2.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship_2)
                MAX_SPEED = 45
                GRAVITY = 20
            }
        }
        shipImg = Public.scaleBitmap(shipImg!!, 3.toByte())
        isReduceShieldStrength = 0
        maxY = Public.screanSize!!.y - shipImg!!.height
        minY = 0
        hitBox = Rect(x.toInt(), y.toInt(), shipImg!!.width, shipImg!!.height)
        reinitLifes()
    }

    private var bitmapIndex: Byte = 0
    private fun nextBitmapStep() {
        if (bitmapIndex.toInt() == 120) bitmapIndex = 0 else bitmapIndex++
        val returned: Bitmap = when {
            bitmapIndex % 6 > 5 -> {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire0)
            }
            bitmapIndex % 6 > 4 -> {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire1)
            }
            bitmapIndex % 6 > 3 -> {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire2)
            }
            bitmapIndex % 6 > 2 -> {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire3)
            }
            bitmapIndex % 6 > 1 -> {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire4)
            }
            else -> { // if (newSpeed > 10) {
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.fire5)
            }
        }
        val udm = (Public.screanSize!!.x / 70 / 2).toFloat()
        if (speed > 30) {
            fireImg = Public.scaleBitmap(returned, 5.toByte())
            fireX = -(6 * udm) - udm * 4
            fireY = udm - udm * 2 // код психапата
        } else if (speed > 15) {
            fireImg = Public.scaleBitmap(returned, 4.toByte())
            fireX = -(6 * udm) - udm * 2
            fireY = udm - udm
        } else {
            fireImg = Public.scaleBitmap(returned, 3.toByte())
            fireX = -(6 * udm)
            fireY = udm
        }
    }

    fun bitmap(): Bitmap? {
        return shipImg
    }

    fun speed(): Float {
        return speed
    }

    fun speed(newSpeed: Float) {
        var newSpeed = newSpeed
        if (newSpeed > MAX_SPEED) {
            newSpeed = MAX_SPEED.toFloat()
        }
        if (newSpeed < MIN_SPEED) {
            newSpeed = MIN_SPEED.toFloat()
        }

//    if (newSpeed > 20) {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__1);
//    } else if (newSpeed > 10) {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__2);
//    } else {
//      bitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship__0);
//    }
        speed = newSpeed
    }

    companion object {
        //region members
        var _userName // = "User" + c_Public._random.nextInt();
                : String? = null
    }

    //endregion
    //endregion
    //region constructors
    init {
        reInit()
    }
}