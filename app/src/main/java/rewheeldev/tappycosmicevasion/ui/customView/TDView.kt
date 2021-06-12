package rewheeldev.tappycosmicevasion.ui.customView

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.OnTouchListener
import rewheeldev.tappycosmicevasion.db.userRecords.UserRecordEntity
import rewheeldev.tappycosmicevasion.model.EnemyShip
import rewheeldev.tappycosmicevasion.model.PlayerShip
import rewheeldev.tappycosmicevasion.model.SpaceDust
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.util.Public
import java.io.IOException
import java.util.*

class TDView(  //endregion
    private val _context: Context,
    private val userRecordRepository: IUserRecordRepository
) : SurfaceView(_context), Runnable {
    //region объявления
    //c_joystick _joystick;
    //    private SharedPreferences prefs;
    //    private SharedPreferences.Editor editor;
    //region Добавление звуков v1
    private val soundPool: SoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder()
            .setMaxStreams(10)
            .build();
    } else {
        SoundPool(10, AudioManager.STREAM_MUSIC, 0)
    }
    private var start = -1
    private var bump = -1
    private var destroyed = -1
    var win = -1

    //endregion
    //region flags
    private var gameEnded = false
    private var level: Byte = 1

    @Volatile
    var playing = false

    //endregion
    //region objects
    private lateinit var player: PlayerShip
    private var dustList = ArrayList<SpaceDust>()
    private var enemyList = ArrayList<EnemyShip>()
    private var timeStarted: Long = 0
    private val screenX: Int
    private val screenY: Int
    private val paint: Paint
    private lateinit var canvas: Canvas
    private val ourHolder: SurfaceHolder
    private var gameThread: Thread? = null

    //region OnTouch
    private var onTouchSpeed: OnTouchListener
    override fun run() {
        while (playing) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        if (gameEnded) return
        var hitDetected = false
        for (i in enemyList.indices) {
            if (Rect.intersects(player.hitBox, enemyList[i].hitBox!!)) {
                hitDetected = true
                enemyList[i].setX(-350)
            }
        }
        if (hitDetected) {
            soundPool.play(bump, 1f, 1f, 0, 0, 1f)
            player.minusLives()
            if (player.lives <= 0) /*количество жизней меньше либо равно нулю */ {
                hitDetected = false
                gameEnded = true
                soundPool.play(destroyed, 1f, 1f, 0, 0, 1f)
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
        for (sd in dustList) {
            sd.update(player.speed())
        }
        player.update()
        for (i in enemyList.indices) {
            enemyList[i].update(player.speed())
        }
        if (!gameEnded) {
            distance += (player.speed() / 1000.0).toFloat()
            timeTaken = System.currentTimeMillis() - timeStarted
        }
        if (distance >= level * 5) {
            startNextLevel()
        }
    }

    private fun startNextLevel() {
        level++
        enemyList.add(EnemyShip(screenX, screenY))
    }

    private fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()
            canvas.drawColor(Color.argb(255, 0, 0, 0))
            paint.color = Color.argb(255, 255, 255, 255)
            //region draw objects
            if (!gameEnded) {

                //region римуем звезды )
                for (i in dustList.indices) {
                    if (dustList[i].downLight) dustList[i].counter++ else dustList[i].counter--
                    if (dustList[i].counter < 10) {
                        dustList[i].downLight = true
                    } else if (dustList[i].counter >= 125) {
                        dustList[i].downLight = false
                    }
                    var opacity = (dustList[i].counter * 5).toShort()
                    if (opacity > 255) opacity = 255 else if (opacity < 50) opacity = 50

                    //region мигание синим и ораньжевым цветом
                    if (dustList[i].counter % 30 > 6) // всего 30 кадров с 6 по 30 работает обычное затухание прозрачности белой звезды
                        paint.color = Color.argb(
                            opacity.toInt(),
                            255,
                            255,
                            255
                        ) else if (dustList[i].counter % 30 > 3) // с 3 по 6 ярко светится синим
                        paint.color = Color.argb(
                            255,
                            0,
                            0,
                            255
                        ) else  // с 0 по 3 ярко светится (типо ораньжевым)
                        paint.color = Color.argb(255, 200, 100, 0)
                    //endregion
                    canvas.drawPoint(dustList[i].x.toFloat(), dustList[i].y.toFloat(), paint)
                }
                //endregion
                paint.color = Color.argb(255, 255, 255, 255)
                canvas.drawBitmap(player.bitmap()!!, player.x, player.y, paint)
                if (player.isTouchSpeed) canvas.drawBitmap(
                    player.fireImg!!,
                    player.x + player.fireX,
                    player.y + player.fireY,
                    paint
                )
                for (i in enemyList.indices) {
                    canvas.drawBitmap(
                        enemyList[i].bitmap!!, //todo occasionally get IndexOfBounds Exception
                        enemyList[i].x.toFloat(),
                        enemyList[i].y.toFloat(),
                        paint
                    )
                }
            }
            //endregion
            if (!gameEnded) {
                paint.textAlign = Paint.Align.LEFT
                paint.color = Color.argb(255, 255, 255, 255)
                paint.textSize = 25f
                canvas.drawText("Level$level", 10f, 20f, paint)
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
                    "Speed:" + (player.speed() * 60).toInt().toShort() + " KMh",
                    (screenX / 3 * 2).toFloat(),
                    (screenY - 20).toFloat(),
                    paint
                )
                if (player.isReduceShieldStrength <= 0) {
                    paint.textSize = 25f
                } else {
                    if (player.isReduceShieldStrength % 10 > 5) {
                        paint.color = Color.argb(255, 255, 0, 0)
                    } else {
                        paint.color = Color.argb(255, 255, 255, 255)
                    }
                    paint.textSize = (25 + player.isReduceShieldStrength).toFloat()
                    player.isReduceShieldStrength--
                }
                canvas.drawText("Shield:" + player.lives, 10f, (screenY - 20).toFloat(), paint)
                if (player.isTouchSpeed) {
                    if (dustList[0].counter % 14 >= 7) paint.color =
                        Color.argb(255, 0, 255, 0) else paint.color = Color.argb(255, 255, 255, 255)
                } else paint.color = Color.argb(100, 255, 255, 255)
                paint.textSize = 60f
                for (i in 1 until screenY / 100) canvas.drawText(
                    ">>>",
                    (screenX - screenX / 8).toFloat(),
                    (i * 100).toFloat(),
                    paint
                )
            } else {
                paint.textSize = 80f
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("Game Over", (screenX / 2).toFloat(), 100f, paint)
                paint.textSize = 25f
                //canvas.drawText("Level" + fastestTime + "s", screenX / 2, 160, paint);
                canvas.drawText(
                    "Time:" + timeTaken / 1000 + "s",
                    (screenX / 2).toFloat(),
                    200f,
                    paint
                )
                canvas.drawText(
                    "Distance remaining:" + distance.toInt().toShort() + " KM",
                    (screenX / 2).toFloat(),
                    240f,
                    paint
                )
                paint.textSize = 80f
                canvas.drawText("Tap to replay!", (screenX / 2).toFloat(), 350f, paint)
            }
            ourHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun control() {
        try {
            Thread.sleep(20)
        } catch (e: InterruptedException) {
        }
    }

    fun pause() {
        playing = false
        try {
            gameThread!!.join()
        } catch (ex: InterruptedException) {
        }
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread!!.start()
    }

    private fun startGame() {
        player = PlayerShip()
        enemyList.clear()
        enemyList.add(EnemyShip(screenX, screenY))
        //soundPool.play(start,1,1,0,10,1);
        val numSpecs: Short = 100
        dustList.clear()
        for (i in 0 until numSpecs) {
            val spec = SpaceDust(screenX.toShort(), screenY.toShort())
            dustList.add(spec)
        }
        distance = 0f
        timeTaken = 0
        timeStarted = System.currentTimeMillis()
        gameEnded = false
    }

    private fun reStartGame() {
        distance = 0f
        level = 1
        timeTaken = 0
        timeStarted = System.currentTimeMillis()
        gameEnded = false
        player.reInit()
        enemyList.clear()
        enemyList.add(EnemyShip(screenX, screenY))
    }

    fun getDistance(): Float /* ???? */ {
        return distance
    }

    companion object {
        //endregion
        //region Счетчики
        var distance = 0f
        var timeTaken: Long = 0
    }

    //endregion
    //endregion
    init {
        //region v1 добавление звука
        try {
            val assetManager = _context.assets
            var descriptor: AssetFileDescriptor = assetManager.openFd("start.ogg")
            start = soundPool.load(descriptor, 0)

//      descriptor = assetManager.openFd("win.ogg");
//      win = soundPool.load(descriptor,0);
            descriptor = assetManager.openFd("bump.ogg")
            bump = soundPool.load(descriptor, 0)
            descriptor = assetManager.openFd("destroyed.ogg")
            destroyed = soundPool.load(descriptor, 0)
        } catch (e: IOException) {
            Log.d(Public.myLogcatTAG, "||| TDView||| error failed to load sound files")
        }
        //_joystick = new c_joystick();
        screenX = Public.screanSize!!.x
        screenY = Public.screanSize!!.y
        ourHolder = holder
        paint = Paint()
        startGame()
        //region OnTouch
        onTouchSpeed = OnTouchListener { _, motionEvent ->
            val actionMask = motionEvent.actionMasked
            player.touchY = motionEvent.y
            val startSpeedX = screenX - screenX / 8
            if (actionMask == MotionEvent.ACTION_MOVE) {
                player.isTouchSpeed = motionEvent.x > startSpeedX
            }
            if (actionMask == MotionEvent.ACTION_POINTER_DOWN) {
                player.isTouchSpeed = motionEvent.x > startSpeedX
            }
            if (actionMask == MotionEvent.ACTION_DOWN) {
                if (gameEnded) {
                    reStartGame()
                    return@OnTouchListener false
                }
                player.isTouchSpeed = motionEvent.x > startSpeedX
            }
            if (actionMask == MotionEvent.ACTION_UP) {
                player.isTouchSpeed = false
            }
            true
        }
        setOnTouchListener(onTouchSpeed)
        //endregion
    }
}