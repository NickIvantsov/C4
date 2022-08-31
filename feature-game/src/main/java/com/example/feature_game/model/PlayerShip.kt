package com.example.feature_game.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import com.example.core_utils.util.scaleBitmap
import com.example.feature_game.R
import com.example.feature_game.tmp.CacheValidationImages
import com.gmail.rewheeldevsdk.api.collision.ICollision2
import com.gmail.rewheeldevsdk.api.models.CollisionInfo
import com.gmail.rewheeldevsdk.internal.joyStick.Joystick

class PlayerShip(
    private val context: Context,
    private val screenX: Int,
    private val screenY: Int,
    private val playerShipType: Int
) : ICollision2 {
    lateinit var shipImg: Bitmap

    lateinit var fireImg: Bitmap

    @set: Synchronized
    @get: Synchronized
    var x = 0f

    @set: Synchronized
    @get: Synchronized
    var y = 0f
    var fireX = 0f
    var fireY = 0f
    var touchY = 50f
    var speed = 0f
        private set
    private var gravity = 25
    private var maxY = screenY
    private var minY = 0
    private var maxX = screenX
    private var minX = 0
    private val minSpeed = 2
    private var maxSpeed = 45
    lateinit var hitBox: Rect
        private set
    var lives = 0
        private set

    var isReduceShieldStrength: Byte = 0
    var isTouchSpeed = false
    var isTouch = false

    fun update(joystick: Joystick) {
        nextBitmapStep()
        if (isTouchSpeed) speed(speed + 0.4.toFloat()) else speed(speed - 0.6.toFloat())

        //region плавное перемещение к пальцу по У
        /* val dist = touchY - y
         if (dist > gravity || dist < 0 - gravity) {
             if (dist > 0) y += gravity.toFloat() else y -= gravity.toFloat()
         } else y += dist*/
        //endregion
        val velocityY = joystick.actuatorY.toFloat() * 20
        val velocityX = joystick.actuatorX.toFloat() * 20
        y += velocityY
        x += velocityX
        //region проверка макс и мин
        if (y < minY) {
            y = minY.toFloat()
        }
        if (y > maxY) {
            y = maxY.toFloat()
        }
        if (x < minX) {
            x = minX.toFloat()
        }
        if (x > maxX) {
            x = maxX.toFloat()
        }
        //endregion
        //region прямоугольник столкновений
        initHitBox()
        //endregion
    }

    private fun initHitBox() {
        hitBox.left = x.toInt()
        hitBox.top = y.toInt()
        hitBox.right = x.toInt() + shipImg.width
        hitBox.bottom = y.toInt() + shipImg.height
    }

    fun minusLives() {
        isReduceShieldStrength = 25
        lives--
    }

    private fun reInitLives() {
        lives = 10_000
    }

    fun reInit() {
        x = 270f //!!! 50;
        y = 250f
        speed(1f)
        shipInitialization()
        shipImg = scaleBitmap(shipImg, 3, screenX)
        isReduceShieldStrength = 0
        maxY = screenY - shipImg.height
        minY = 0
        maxX = screenX - shipImg.width
        minX = 0
        hitBox = Rect(x.toInt(), y.toInt(), shipImg.width, shipImg.height)
        reInitLives()
    }

    private fun shipInitialization() {
        shipImg = getShipBitmap(playerShipType)
        maxSpeed = getShipMaxSpeed(playerShipType)
        gravity = getShipGravity(playerShipType)
    }

    private fun getShipBitmap(playerShipType: Int): Bitmap {
        return when (playerShipType) {
            0 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.spaceship_1)
            }
            1 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.spaceship)
            }
            2 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.spaceship_2)
            }
            else -> {
                throw Exception("Unknown ship type")
            }
        }
    }

    private fun getShipMaxSpeed(playerShipType: Int): Int {
        return when (playerShipType) {
            0 -> {
                60
            }
            1 -> {
                30
            }
            2 -> {
                45
            }
            else -> {
                throw Exception("Unknown ship type")
            }
        }
    }

    private fun getShipGravity(playerShipType: Int): Int {
        return when (playerShipType) {
            0 -> {
                10
            }
            1 -> {
                40
            }
            2 -> {
                20
            }
            else -> {
                throw Exception("Unknown ship type")
            }
        }
    }

    private var bitmapIndex: Byte = 0

    private fun nextBitmapStep() {
        if (bitmapIndex.toInt() == 120) bitmapIndex = 0 else bitmapIndex++
        val returned: Bitmap = when {
            bitmapIndex % 6 > 5 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire0)
            }
            bitmapIndex % 6 > 4 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire1)
            }
            bitmapIndex % 6 > 3 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire2)
            }
            bitmapIndex % 6 > 2 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire3)
            }
            bitmapIndex % 6 > 1 -> {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire4)
            }
            else -> { // if (newSpeed > 10) {
                BitmapFactory.decodeResource(context.resources, R.drawable.fire5)
            }
        }
        val udm = (screenX / 70 / 2).toFloat()
        if (speed > 30) {
            fireImg = scaleBitmap(returned, 5, screenX)
            fireX = -(6 * udm) - udm * 4
            fireY = udm - udm * 2 // код психапата
        } else if (speed > 15) {
            fireImg = scaleBitmap(returned, 4, screenX)
            fireX = -(6 * udm) - udm * 2
            fireY = udm - udm
        } else {
            fireImg = scaleBitmap(returned, 3, screenX)
            fireX = -(6 * udm)
            fireY = udm
        }
    }

    private fun speed(newSpeed: Float) {
        var newSpeedTmp = newSpeed
        if (newSpeed > maxSpeed) {
            newSpeedTmp = maxSpeed.toFloat()
        }
        if (newSpeed < minSpeed) {
            newSpeedTmp = minSpeed.toFloat()
        }

        speed = newSpeedTmp
    }

    init {
        reInit()
        CacheValidationImages.playerShip = CollisionInfo(shipImg)
        CacheValidationImages.playerShip.initializeImageBlocks()
    }

    override fun getCurrentFrame(): CollisionInfo = CacheValidationImages.playerShip

    override fun getFrameHitBox(): Rect = hitBox
}