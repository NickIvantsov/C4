package ua.yandex.jere184.c4tappydefender.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.util.Public

class PlayerShip {
    private var shipImg: Bitmap? = null

    var fireImg: Bitmap? = null
    var x = 0f
    var y = 0f
    var fireX = 0f
    var fireY = 0f
    var touchY = 50f
    private var speed = 0f
    private var gravity = 25
    private var maxY = 0
    private var minY = 0
    private val minSpeed = 2
    private var maxSpeed = 45
    lateinit var hitBox: Rect
        private set
    var lives = 0
        private set

    var isReduceShieldStrength: Byte = 0
    var isTouchSpeed = false

    fun update() {
        nextBitmapStep()
        if (isTouchSpeed) speed(speed + 0.4.toFloat()) else speed(speed - 0.6.toFloat())

        //region плавное перемещение к пальцу по У
        val dist = touchY - y
        if (dist > gravity || dist < 0 - gravity) {
            if (dist > 0) y += gravity.toFloat() else y -= gravity.toFloat()
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
        hitBox.left = x.toInt()
        hitBox.top = y.toInt()
        hitBox.right = x.toInt() + shipImg!!.width
        hitBox.bottom = y.toInt() + shipImg!!.height
        //endregion
    }

    fun minusLives() {
        isReduceShieldStrength = 25
        lives--
    }

    private fun reInitLives() {
        lives = 10
    }

    fun reInit() {
        x = 70f //!!! 50;
        y = 250f
        speed(1f)
        when (Public.playerShipType) {
            0.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship_1)
                maxSpeed = 60
                gravity = 10
            }
            1.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship)
                maxSpeed = 30
                gravity = 40
            }
            2.toByte() -> {
                shipImg =
                    BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship_2)
                maxSpeed = 45
                gravity = 20
            }
        }
        shipImg = Public.scaleBitmap(shipImg!!, 3.toByte())
        isReduceShieldStrength = 0
        maxY = Public.screanSize!!.y - shipImg!!.height
        minY = 0
        hitBox = Rect(x.toInt(), y.toInt(), shipImg!!.width, shipImg!!.height)
        reInitLives()
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

    private fun speed(newSpeed: Float) {
        var newSpeed = newSpeed
        if (newSpeed > maxSpeed) {
            newSpeed = maxSpeed.toFloat()
        }
        if (newSpeed < minSpeed) {
            newSpeed = minSpeed.toFloat()
        }

        speed = newSpeed
    }

    init {
        reInit()
    }
}