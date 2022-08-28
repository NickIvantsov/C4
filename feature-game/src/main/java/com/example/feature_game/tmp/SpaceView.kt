package com.example.feature_game.tmp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.text.format.DateFormat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.OnTouchListener
import com.example.core.interactor.SpaceDustUseCase
import com.example.core_utils.util.logging.extensions.logD
import com.example.feature_game.R
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.repository.IUserRecordRepository
import com.gmail.rewheeldevsdk.api.util.hitBoxDetection
import com.gmail.rewheeldevsdk.internal.joyStick.Joystick
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SpaceView(
    //endregion
    context: Context,
    private val userRecordRepository: IUserRecordRepository,
    private val random: Random,
    private val screenSize: Point,
    private val playerShipType: Int,
    private val meteoriteRepository: IMeteoriteRepository,
    private val spaceDustInteractor: SpaceDustUseCase,
    private val spaceViewModel: SpaceViewModel,
    attrs: AttributeSet? = null,
) : SurfaceView(context, attrs), Runnable {

    //endregion
    //region flags

    @Volatile
    var playing = false

    //endregion
    //region objects
    private val screenX: Int = screenSize.x
    private val screenY: Int = screenSize.y
    private val paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private val ourHolder: SurfaceHolder = holder
    private var gameThread: Thread? = null

    private var onTouchSpeed: OnTouchListener = OnTouchListener { view, motionEvent ->


        val pointerIndex: Int = motionEvent.actionIndex
        val pointerCount: Int = motionEvent.pointerCount

        val actionMask = motionEvent.actionMasked

        spaceViewModel.player.touchY = motionEvent.y

        val startSpeedX = screenX - screenX / 8

        if (actionMask == MotionEvent.ACTION_MOVE) {
            if (joystick.isPressed) {
                joystick.setActuator(motionEvent.x.toDouble(), motionEvent.y.toDouble())
            }
            val displacement: Float = (motionEvent.x - centerX) + (motionEvent.y - centerY)
            if (displacement < baseRadius) {
                newX = motionEvent.x
                newY = motionEvent.y
            }

            spaceViewModel.player.isTouchSpeed = motionEvent.x > startSpeedX
        }
        if (actionMask == MotionEvent.ACTION_POINTER_DOWN) {
            downPI = pointerIndex
        }
        if (actionMask == MotionEvent.ACTION_POINTER_UP) {
            upPI = pointerIndex
        }
        spaceViewModel.player.isTouchSpeed = pointerCount > 1
        if (actionMask == MotionEvent.ACTION_DOWN) {
            joystick.outerCircleCenterPositionX = motionEvent.x.toInt()
            joystick.outerCircleCenterPositionY = motionEvent.y.toInt()
            if (joystick.isPressed(motionEvent.x.toDouble(), motionEvent.y.toDouble())) {
                joystick.isPressed = true
            }
            spaceViewModel.player.isTouch = true
            centerX = motionEvent.x
            centerY = motionEvent.y
            baseRadius = (Math.min(width, height) / 6).toFloat()
            hatRadius = (Math.min(width, height) / 10).toFloat()

            if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.ENDED) {
                spaceViewModel.reStartGame(screenX, screenY, random, screenSize)
                return@OnTouchListener false
            }
            spaceViewModel.player.isTouchSpeed = motionEvent.x > startSpeedX
        }

        if (actionMask == MotionEvent.ACTION_UP) {
            joystick.isPressed = false
            joystick.resetActuator()
//            spaceViewModel.player.isTouchSpeed = false
            spaceViewModel.player.isTouch = false
            view.performClick()
        }
        true
    }

    init {
        setNewGameStatus(com.example.core_utils.util.logging.GameStatus.NOT_START)
        spaceViewModel.startGame(
            context.applicationContext,
            screenX,
            screenY,
            playerShipType,
            random,
            screenSize
        )
        setOnTouchListener(onTouchSpeed)
    }

    var centerX = 100F
    var centerY = 100F
    var baseRadius = 200F
    var hatRadius = 100F
    private var newX: Float = 0F
    private var newY: Float = 0F
    var upPI = 0
    var downPI = 0

    val joystick = Joystick(175, 650, 70, 40)

    private val background: Background = Background()
    private val shipManager: ShipManager = ShipManager(spaceViewModel)
    private val meteoritesManager: MeteoritesManager = MeteoritesManager(meteoriteRepository)
    private val spaceDustManager: SpaceDustManager =
        SpaceDustManager(spaceDustInteractor, spaceViewModel)


    override fun run() {
        while (playing) {

            update()
            draw()
            control()

        }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("TAG_6", "CoroutineExceptionHandler got $exception", exception)
    }

    private fun update() {
        if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.ENDED) return
        joystick.update()
        var hitDetected = false

        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            val meteorite = meteoriteRepository.getMeteoriteByIndex(i)

            val startTime = System.currentTimeMillis()
            if (hitBoxDetection(spaceViewModel.player, meteorite)) {
                hitDetected = true
//                перемещение метеорита с которым столкнулись в отрицательные координаты по оси X за пределы экрана
//                для определения алгоритмом что метеорт долетел до кронца экрана и должен быдет перерисоваться
                meteorite.x = -350
            }

            val timeCost = System.currentTimeMillis() - startTime
            if (timeCost > 35)
                Log.d("TAG_6", "time cost: $timeCost")
        }
        if (hitDetected) {
            CoroutineScope(Dispatchers.IO).launch {
                spaceViewModel.playSound(com.example.core_utils.util.logging.SoundName.BUMP)
            }

            spaceViewModel.player.minusLives()
            if (spaceViewModel.player.lives <= 0) /*количество жизней меньше либо равно нулю */ {
                hitDetected = false
                setNewGameStatus(com.example.core_utils.util.logging.GameStatus.ENDED)
                CoroutineScope(Dispatchers.IO).launch {
                    spaceViewModel.playSound(com.example.core_utils.util.logging.SoundName.DESTROYED)
                }
                val currentTimeStr =
                    DateFormat.format("dd.MM hh:mm", Date(System.currentTimeMillis()))
                userRecordRepository.insert(
                    com.example.model.UserRecordEntity(
                        currentTimeStr.toString(),
                        distance,
                        timeTaken
                    )
                )
            }
        }
        for (spaceDust in spaceDustInteractor.getAll()) {
            spaceDust.update(spaceViewModel.player.speed)
        }
        spaceViewModel.player.update(joystick)
        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            meteoriteRepository.getMeteoriteByIndex(i)
                .update(spaceViewModel.player.speed) //IndexOutOfBoundsException после перезапуска игры (1 раз)
        }
        if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.PLAYING) {
            distance += (spaceViewModel.player.speed / 1000.0).toFloat()
            timeTaken = System.currentTimeMillis() - spaceViewModel.timeStarted
        }
        if (distance >= getLevel() * 5) {
            spaceViewModel.startNextLevel(screenX, screenY, random, screenSize)
        }
    }

    private fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()

            background.draw(canvas)

            paint.color = Color.WHITE
            if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.PLAYING) {
                gameProcess()
            } else {
                gameOverProcess()
            }
            ourHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun gameOverProcess() {
        paint.textSize = 80f
        paint.textAlign = Paint.Align.CENTER
        val gameOverStr = getResString(R.string.game_ower)

        canvas.drawText(gameOverStr, (screenX / 2).toFloat(), 100f, paint)

        paint.textSize = 25f
        //canvas.drawText("Level" + fastestTime + "s", screenX / 2, 160, paint);
        val timeStr = getResString(R.string.time) + timeTaken / 1000 + "s"
        canvas.drawText(
            timeStr,
            (screenX / 2).toFloat(),
            200f,
            paint
        )
        val result =
            "${getResString(R.string.distance_remaining_msg)}  ${distance.toInt()} ${
                getResString(R.string.km)
            }"
        canvas.drawText(
            result,
            (screenX / 2).toFloat(),
            240f,
            paint
        )
        paint.textSize = 80f

        canvas.drawText(
            context.getString(R.string.tap_to_replay_msg),
            (screenX / 2).toFloat(),
            350f,
            paint
        )
    }

    private fun gameProcess() {
        spaceDustManager.drawSpaceDust(canvas)
        shipManager.draw(canvas)
        meteoritesManager.draw(canvas)

        if (spaceViewModel.player.isTouch) {
            joystick.draw(canvas)
        }


        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.argb(255, 255, 255, 255)
        paint.textSize = 25f
        canvas.drawText("Level${getLevel()}", 20f, 30f, paint)
        canvas.drawText(
            "Time:" + timeTaken / 1000 + "s",
            (screenX / 2).toFloat(),
            20f,
            paint
        )
        canvas.drawText(
            "Distance:" + distance.toInt().toShort() + "KM",
            (screenX / 3).toFloat(),
            (screenY - 20).toFloat(),
            paint
        )
        canvas.drawText(
            "Speed:" + (spaceViewModel.player.speed * 60).toInt().toShort() + " KMh",
            (screenX / 3 * 2).toFloat(),
            (screenY - 20).toFloat(),
            paint
        )
        if (spaceViewModel.player.isReduceShieldStrength <= 0) {
            paint.textSize = 25f
        } else {
            if (spaceViewModel.player.isReduceShieldStrength % 10 > 5) {
                paint.color = Color.argb(255, 255, 0, 0)
            } else {
                paint.color = Color.argb(255, 255, 255, 255)
            }
            paint.textSize = (25 + spaceViewModel.player.isReduceShieldStrength).toFloat()
            spaceViewModel.player.isReduceShieldStrength--
        }
        canvas.drawText(
            "Shield:" + spaceViewModel.player.lives,
            10f,
            (screenY - 20).toFloat(),
            paint
        )
        /*if (spaceViewModel.player.isTouchSpeed) {
            if (spaceDustRepository.getByIndex(0).counter % 14 >= 7) paint.color =
                Color.argb(255, 0, 255, 0) else paint.color = Color.argb(255, 255, 255, 255)
        } else paint.color = Color.argb(100, 255, 255, 255)
        paint.textSize = 60f
        for (i in 1 until screenY / 100) canvas.drawText(
            ">>>",
            (screenX - screenX / 8).toFloat(),
            (i * 100).toFloat(),
            paint
        )*/
    }

    private fun getResString(id: Int): String {
        return context.getString(id)
    }

    private fun drawMeteorites(
        meteoriteRepository: IMeteoriteRepository,
        canvas: Canvas,
        paint: Paint
    ) {
        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            val meteorite =
                meteoriteRepository.getMeteoriteByIndex(i) //IndexOutOfBoundsException при рестарте игры (1 раз)
            canvas.drawBitmap(
                meteorite.bitmap, //todo occasionally get IndexOfBounds Exception (3 times)
                meteorite.x.toFloat(),
                meteorite.y.toFloat(),
                paint
            )
        }
    }

    private fun control() {
        try {
            Thread.sleep(20)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun pause() {
        playing = false
        try {
            gameThread!!.join()
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread!!.start()
    }

    //endregion
    //endregion


    private fun setNewGameStatus(status: com.example.core_utils.util.logging.GameStatus) {
        spaceViewModel.gameStatus = status
    }

    private fun getGameStatus(): com.example.core_utils.util.logging.GameStatus {
        return spaceViewModel.gameStatus
    }

    private fun getLevel(): Int {
        return spaceViewModel.level
    }

    private fun log(msg: String) {
        logD(msg)
    }

    companion object {
        //endregion
        //region Счетчики
        var distance = 0f
        var timeTaken: Long = 0
        private const val TAG = "TDView"
        const val NUMBER_OF_FRAMES = 30
    }
}