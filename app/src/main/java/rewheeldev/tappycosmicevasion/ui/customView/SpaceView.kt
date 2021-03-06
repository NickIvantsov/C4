package rewheeldev.tappycosmicevasion.ui.customView

import android.content.Context
import android.graphics.*
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.OnTouchListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rewheeldev.tappycosmicevasion.R
import rewheeldev.tappycosmicevasion.db.userRecords.UserRecordEntity
import rewheeldev.tappycosmicevasion.logging.logD
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import rewheeldev.tappycosmicevasion.repository.ISpaceDustRepository
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.util.GameStatus
import rewheeldev.tappycosmicevasion.util.SoundName
import java.util.*

class SpaceView(
    //endregion
    context: Context,
    private val userRecordRepository: IUserRecordRepository,
    private val random: Random,
    private val screenSize: Point,
    private val playerShipType: Int,
    private val meteoriteRepository: IMeteoriteRepository,
    private val spaceDustRepository: ISpaceDustRepository,
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

    //region OnTouch
    private var onTouchSpeed: OnTouchListener = OnTouchListener { view, motionEvent ->

        val actionMask = motionEvent.actionMasked

        spaceViewModel.player.touchY = motionEvent.y

        val startSpeedX = screenX - screenX / 8

        if (actionMask == MotionEvent.ACTION_MOVE) {
            spaceViewModel.player.isTouchSpeed = motionEvent.x > startSpeedX
        }

        if (actionMask == MotionEvent.ACTION_POINTER_DOWN) {
            spaceViewModel.player.isTouchSpeed = motionEvent.x > startSpeedX
        }

        if (actionMask == MotionEvent.ACTION_DOWN) {
            if (getGameStatus() == GameStatus.ENDED) {
                spaceViewModel.reStartGame(screenX, screenY, random, screenSize)
                return@OnTouchListener false
            }
            spaceViewModel.player.isTouchSpeed = motionEvent.x > startSpeedX
        }

        if (actionMask == MotionEvent.ACTION_UP) {
            spaceViewModel.player.isTouchSpeed = false
            view.performClick()
        }
        true
    }

    override fun run() {
        while (playing) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        if (getGameStatus() == GameStatus.ENDED) return

        var hitDetected = false

        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            val meteorite = meteoriteRepository.getMeteoriteByIndex(i)
            if (Rect.intersects(spaceViewModel.player.hitBox, meteorite.hitBox)) {
                hitDetected = true
                meteorite.x = -350
            }
        }
        if (hitDetected) {
            CoroutineScope(Dispatchers.IO).launch {
                spaceViewModel.playSound(SoundName.BUMP)
            }

            spaceViewModel.player.minusLives()
            if (spaceViewModel.player.lives <= 0) /*количество жизней меньше либо равно нулю */ {
                hitDetected = false
                setNewGameStatus(GameStatus.ENDED)
                CoroutineScope(Dispatchers.IO).launch {
                    spaceViewModel.playSound(SoundName.DESTROYED)
                }
                val currentTimeStr =
                    DateFormat.format("dd.MM hh:mm", Date(System.currentTimeMillis()))
                userRecordRepository.insert(
                    UserRecordEntity(
                        currentTimeStr.toString(),
                        distance,
                        timeTaken
                    )
                )
            }
        }
        for (spaceDust in spaceDustRepository.getAll()) {
            spaceDust.update(spaceViewModel.player.speed)
        }
        spaceViewModel.player.update()
        for (i in 0 until meteoriteRepository.getSizeMeteoriteList()) {
            meteoriteRepository.getMeteoriteByIndex(i)
                .update(spaceViewModel.player.speed) //IndexOutOfBoundsException после перезапуска игры (1 раз)
        }
        if (getGameStatus() == GameStatus.PLAYING) {
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

            val backgroundColor = Color.argb(255, 0, 0, 0)
            canvas.drawColor(backgroundColor)

            paint.color = Color.WHITE
            if (getGameStatus() == GameStatus.PLAYING) {
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
        drawSpaceDust()
        paint.color = Color.argb(255, 255, 255, 255)

        val playerShipDrawInfo = spaceViewModel.getPlayerShipDrawInfo()

        canvas.drawBitmap(
            playerShipDrawInfo.ship,
            playerShipDrawInfo.shipX,
            playerShipDrawInfo.shipY,
            paint
        )

        if (spaceViewModel.player.isTouchSpeed) canvas.drawBitmap(
            playerShipDrawInfo.fire,
            playerShipDrawInfo.shipX + playerShipDrawInfo.fireX,
            playerShipDrawInfo.shipY + playerShipDrawInfo.fireY,
            paint
        )
        drawMeteorites()

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
        if (spaceViewModel.player.isTouchSpeed) {
            if (spaceDustRepository.getByIndex(0).counter % 14 >= 7) paint.color =
                Color.argb(255, 0, 255, 0) else paint.color = Color.argb(255, 255, 255, 255)
        } else paint.color = Color.argb(100, 255, 255, 255)
        paint.textSize = 60f
        for (i in 1 until screenY / 100) canvas.drawText(
            ">>>",
            (screenX - screenX / 8).toFloat(),
            (i * 100).toFloat(),
            paint
        )
    }

    private fun getResString(id: Int): String {
        return context.getString(id)
    }

    private fun drawMeteorites() {
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

    private fun drawSpaceDust() {
        for (spaceDustElementIndex in 0 until spaceDustRepository.getSize()) {

            val currentSpaceDustElement = spaceDustRepository.getByIndex(spaceDustElementIndex)

            if (currentSpaceDustElement.downLight) currentSpaceDustElement.counter++ else currentSpaceDustElement.counter--

            if (currentSpaceDustElement.counter < 10) {
                currentSpaceDustElement.downLight = true
            } else if (currentSpaceDustElement.counter >= 125) {
                currentSpaceDustElement.downLight = false
            }

            val opacity = spaceViewModel.getSpaceDustOpacity(currentSpaceDustElement.counter)
            //region мигание синим и ораньжевым цветом
            paint.color = spaceViewModel.getCurrentSpaceDustColor(
                opacity,
                currentSpaceDustElement.counter
            )
            //endregion
            canvas.drawPoint(
                currentSpaceDustElement.x.toFloat(),
                currentSpaceDustElement.y.toFloat(),
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
    init {
        setNewGameStatus(GameStatus.NOT_START)
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

    private fun setNewGameStatus(status: GameStatus) {
        spaceViewModel.gameStatus = status
    }

    private fun getGameStatus(): GameStatus {
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