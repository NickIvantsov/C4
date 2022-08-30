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
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.feature_game.ui.colors.WHITE
import com.example.feature_game.ui.screens.GameOverScreen
import com.example.feature_game.ui.screens.GameScreen
import com.example.repository.IUserRecordRepository
import com.gmail.rewheeldevsdk.api.util.hitBoxDetection
import com.gmail.rewheeldevsdk.api.util.hitCircleBoxDetection
import com.gmail.rewheeldevsdk.internal.joyStick.Joystick
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SpaceView @JvmOverloads constructor(
    //endregion
    context: Context,
    private val userRecordRepository: IUserRecordRepository,
    private val random: Random,
    private val screenSize: Point,
    private val playerShipType: Int,
    private val meteoriteRepository: IMeteoriteRepository,
    private val spaceDustInteractor: SpaceDustUseCase,
    private val spaceViewModel: SpaceViewModel,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : SurfaceView(context, attributeSet, defStyle), Runnable {
    var debugEnable: Boolean = false

    // This variable tracks the game frame rate
    private var fps: Long = 0
    var fpsDivider = 20L
    // This is used to help calculate the fps
    private var timeThisFrame: Long = 0

    //endregion

    //region flags

    @Volatile
    var playing = false


    //endregion
    //region objects
    private var screenX = screenSize.x

    fun setScreenX(screenX: Int) {
        this.screenX = screenX
    }

    private var screenY = screenSize.y
    fun setScreenY(screenY: Int) {
        this.screenY = screenY
    }

    private val paint: Paint = Paint()
    private lateinit var canvas: Canvas
    private val ourHolder: SurfaceHolder = holder
    private var gameThread: Thread? = null
    private val gameOverScreen by lazy { GameOverScreen(context, canvas) }
    private val gameScreen by lazy { GameScreen(canvas) }
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
                spaceViewModel.reStartGame(screenX, screenY, random)
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
//        Log.d("SCREEN_SIZE", "screenSize: $screenSize | screenX: $screenX, screenY: $screenY")
        spaceViewModel.startGame(
            context.applicationContext,
            screenX,
            screenY,
            playerShipType,
            random
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
    private val meteoritesManager: MeteoritesManager by lazy {
        MeteoritesManager(
            meteoriteRepository,
            canvas
        )
    }
    private val spaceDustManager: SpaceDustManager =
        SpaceDustManager(spaceDustInteractor, spaceViewModel)


    override fun run() {
        while (playing) {
            // Capture the current time in milliseconds in startFrameTime
            val startFrameTime = System.currentTimeMillis()
            update()
            draw()
            control()
            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("TAG_6", "CoroutineExceptionHandler got $exception", exception)
    }
    val scope = CoroutineScope(Dispatchers.IO)
    private fun update() {
        scope.launch {
            if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.ENDED) return@launch

            joystick.update()
            var hitDetected = false

            for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
                val meteorite = meteoriteRepository.getMeteoriteByIndex(i)
                if (meteorite.x < 0 || meteorite.y > screenY) continue
                val startTime = System.currentTimeMillis()
//                if (hitBoxDetection(spaceViewModel.player, meteorite)) {
                if (hitCircleBoxDetection(spaceViewModel.player, meteorite)) {
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
                spaceViewModel.playSound(com.example.core_utils.util.logging.SoundName.BUMP)

                spaceViewModel.player.minusLives()
                if (spaceViewModel.player.lives <= 0) /*количество жизней меньше либо равно нулю */ {
                    hitDetected = false
                    setNewGameStatus(com.example.core_utils.util.logging.GameStatus.ENDED)
                    spaceViewModel.playSound(com.example.core_utils.util.logging.SoundName.DESTROYED)
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
                spaceViewModel.startNextLevel(screenX, screenY, random)
            }
        }
    }

    private fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()

            background.draw(canvas)

            paint.color = WHITE

            if (getGameStatus() == com.example.core_utils.util.logging.GameStatus.PLAYING) {
                gameScreen.showGameScreen(
                    screenX = screenX,
                    screenY = screenY,
                    speed = (spaceViewModel.player.speed * 60).toInt().toShort(),
                    isReduceShieldStrength = spaceViewModel.player.isReduceShieldStrength,
                    level = getLevel(),
                    lives = spaceViewModel.player.lives,
                    isTouch = spaceViewModel.player.isTouch,
                    changReduceShieldStrength = { spaceViewModel.player.isReduceShieldStrength-- },
                    drawSpaceDust = { spaceDustManager.drawSpaceDust(canvas) },
                    drawShip = { shipManager.draw(canvas, debugEnable) },
                    drawMeteorites = { meteoritesManager.draw(debugEnable) },
                    drawJoystick = { joystick.draw(canvas) }
                )
            } else {
                gameOverScreen.showGameOverScreen(screenX)
            }

            if (debugEnable) {
                if (fps < 10) {
                    // Make the text a bit bigger
                    paint.textSize = 65F
                    // Choose the brush color for drawing
                    paint.color = Color.argb(255, 255, 0, 0)
                    // Display the current fps on the screen
                    canvas.drawText("FPS:$fps", 120F, 60F, paint)


                } else {
                    // Make the text a bit bigger
                    paint.textSize = 45F
                    // Choose the brush color for drawing
                    paint.color = Color.argb(255, 249, 129, 0)
                    // Display the current fps on the screen
                    canvas.drawText("FPS:$fps", 120F, 40F, paint)

                }
            }

            ourHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun control() {
        try {
            Thread.sleep(fpsDivider)
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