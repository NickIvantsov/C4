package rewheeldev.tappycosmicevasion.ui.fragments.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.feature_game.tmp.Background
import com.example.feature_game.ui.colors.WHITE
import rewheeldev.tappycosmicevasion.R


class MainView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    SurfaceView(context, attrs, defStyle),
    SurfaceHolder.Callback, Runnable {
    @Volatile
    var playing = false
    var isExplosion = false

    private lateinit var canvas: Canvas
    private val ourHolder: SurfaceHolder = holder
    private var gameThread: Thread? = null
    private val paint: Paint = Paint()
    private val background: Background = Background()

    // This variable tracks the game frame rate
    var fps: Long = 0

    // This is used to help calculate the fps
    private var timeThisFrame: Long = 0

    // These next two values can be anything you like
    // As long as the ratio doesn't distort the sprite too much
    private val explosionFrameWidth = 650
    private val explosionFrameHeight = 650

    private val fireFrameWidth = 200
    private val fireFrameHeight = 200

    private val starFrameWidth = 200
    private val starFrameHeight = 200

    private val star2FrameWidth = 200
    private val star2FrameHeight = 200

    private val star3FrameWidth = 200
    private val star3FrameHeight = 400


    // He starts 10 pixels from the left
    var explosionXPosition = 500f

    var fireXPosition = 100f
    var fireYPosition = 100f

    var starXPosition = 1200F
    var starYPosition = 100f

    var star2XPosition = 1200F
    var star2YPosition = 300f

    var star3XPosition = 1200F
    var star3YPosition = 500f

    // How many frames are there on the sprite sheet?
    private val frameCount = 48
    private val fireFrameCount = 25
    private val starFrameCount = 30
    private val star2FrameCount = 15
    private val star3FrameCount = 16
    private val star3FrameRow = 2
    private val star3FrameColm = 8

    // Start at the first frame - where else?
    private var explosionCurrentFrame = 0
    private var fireCurrentFrame = 0
    private var starCurrentFrame = 0
    private var star2CurrentFrame = 0
    private var star3CurrentFrame = 0

    // What time was it when we last changed frames
    private var lastFrameChangeTime: Long = 0

    // How long should each frame last
    private val frameLengthInMilliseconds = 70


    // He can walk at 150 pixels per second
    var walkSpeedPerSecond = 10f

    // A rectangle to define an area of the
    // sprite sheet that represents 1 frame
    private val explosionFrameToDraw = Rect(
        0,
        0,
        explosionFrameWidth,
        explosionFrameHeight
    )
    private val fireFrameToDraw = Rect(
        0,
        0,
        fireFrameWidth,
        fireFrameHeight
    )
    private val starFrameToDraw = Rect(
        0,
        0,
        starFrameWidth,
        starFrameHeight
    )
    private val star2FrameToDraw = Rect(
        0,
        0,
        star2FrameWidth,
        star2FrameHeight
    )
    private val star3FrameToDraw = Rect(
        0,
        0,
        star3FrameWidth,
        star3FrameHeight
    )

    // A rect that defines an area of the screen
    // on which to draw
    var whereToDrawExplosion = RectF(
        explosionXPosition, 0F,
        explosionXPosition + explosionFrameWidth,
        explosionFrameHeight.toFloat()
    )
    var whereToDrawFire = RectF(
        fireXPosition, fireYPosition,
        fireXPosition + fireFrameWidth,
        fireFrameHeight.toFloat()
    )
    var whereToDrawStar = RectF(
        starXPosition, starYPosition,
        starXPosition + fireFrameWidth,
        starFrameHeight.toFloat()
    )

    var whereToDrawStar2 = RectF(
        star2XPosition, star2YPosition,
        star2XPosition + star2FrameWidth,
        star2FrameHeight.toFloat()
    )
    var whereToDrawStar3 = RectF(
        star3XPosition, star3YPosition,
        star3XPosition + star3FrameWidth,
        star3FrameHeight.toFloat()
    )

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    fun getCurrentFrame() {
        val time = System.currentTimeMillis()
//        if (isExplosion) { // Only animate if bob is moving
//
//        }
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time
            Log.d("TAG_1", "getCurrentFrame 1| currentFrame: $explosionCurrentFrame")
            explosionCurrentFrame++
            Log.d("TAG_1", "getCurrentFrame 2| currentFrame: $explosionCurrentFrame")
            if (explosionCurrentFrame >= frameCount) {
                explosionCurrentFrame = 0
//                isExplosion = false
            }

//           fire
            fireCurrentFrame++
            if (fireCurrentFrame >= fireFrameCount) {
                fireCurrentFrame = 0
            }

//           star
            starCurrentFrame++
            if (starCurrentFrame >= starFrameCount) {
                starCurrentFrame = 0
            }
//           star2
            star2CurrentFrame++
            if (star2CurrentFrame >= star2FrameCount - 1) {
                star2CurrentFrame = 0
            }
            //           star3
            star3CurrentFrame++
            if (star3CurrentFrame >= star3FrameCount) {
                star3CurrentFrame = 0
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
//        frameToDraw.left = currentFrame * frameWidth
//        frameToDraw.right = frameToDraw.left + frameWidth
    }


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

    fun update() {
        // If bob is moving (the player is touching the screen)
        // then move him to the right based on his target speed and the current fps.
        if (isExplosion) {
            explosionXPosition = explosionXPosition + (walkSpeedPerSecond / fps);
        }
    }

    fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()
            background.draw(canvas)
            paint.color = WHITE
            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 249, 129, 0))

            // Make the text a bit bigger
            paint.setTextSize(45F)

            // Display the current fps on the screen
            canvas.drawText("FPS:$fps", 20F, 40F, paint)


// New drawing code goes here
            whereToDrawExplosion.set(
                explosionXPosition,
                0f,
                (explosionXPosition + explosionFrameWidth),
                explosionFrameHeight.toFloat()
            )


            getCurrentFrame()
//            Log.d("TAG_1","currentFrame: $currentFrame")
            canvas.drawBitmap(
                explosionBitmaps[explosionCurrentFrame],
                explosionFrameToDraw,
                whereToDrawExplosion, paint
            )

            whereToDrawFire.set(
                fireXPosition,
                fireYPosition,
                (fireXPosition + fireFrameWidth),
                (fireFrameHeight.toFloat() + fireYPosition)
            )
            canvas.drawBitmap(
                fireBitmaps[fireCurrentFrame],
                fireFrameToDraw,
                whereToDrawFire, paint
            )

            whereToDrawStar.set(
                starXPosition,
                starYPosition,
                (starXPosition + starFrameWidth),
                (starFrameHeight.toFloat() + starYPosition)
            )

            canvas.drawBitmap(
                starBitmaps[starCurrentFrame],
                starFrameToDraw,
                whereToDrawStar, paint
            )

            whereToDrawStar2.set(
                star2XPosition,
                star2YPosition,
                (star2XPosition + star2FrameWidth),
                (star2FrameHeight.toFloat() + star2YPosition)
            )

            canvas.drawBitmap(
                star2Bitmaps[star2CurrentFrame],
                star2FrameToDraw,
                whereToDrawStar2, paint
            )
            whereToDrawStar3.set(
                star3XPosition,
                star3YPosition,
                (star3XPosition + star3FrameWidth),
                (star3FrameHeight.toFloat() + star3YPosition)
            )

            canvas.drawBitmap(
                star3Bitmaps[star3CurrentFrame],
                star3FrameToDraw,
                whereToDrawStar3, paint
            )
            paint.style = Paint.Style.STROKE
            paint.color = Color.RED

            ourHolder.unlockCanvasAndPost(canvas)
        }
    }

    val explosionBitmaps = mutableListOf<Bitmap>()
    val fireBitmaps = mutableListOf<Bitmap>()
    val starBitmaps = mutableListOf<Bitmap>()
    val star2Bitmaps = mutableListOf<Bitmap>()
    val star3Bitmaps = mutableListOf<Bitmap>()


    fun initBitmap(
        context: Context,
        explosionBitmaps: MutableList<Bitmap>,
        fireBitmaps: MutableList<Bitmap>,
        starBitmaps: MutableList<Bitmap>,
        star2Bitmaps: MutableList<Bitmap>,
        star3Bitmaps: MutableList<Bitmap>,
    ) {
        val explosionBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.sprite_explosion)
        val partImgSizeX = explosionBitmap.width / 8 // 1920/8 = 240;
        val partImgSizeY = explosionBitmap.height / 6 // 1440 / 6 = 240;

        for (raw in 0 until 6) {
            for (colm in 0..7) {
                var bitmap = Bitmap.createBitmap(
                    explosionBitmap,
                    colm * partImgSizeX,
                    raw * partImgSizeY,
                    partImgSizeX,
                    partImgSizeY
                )
//                bitmapExplosion = bitmap
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    explosionFrameWidth,
                    explosionFrameHeight,
                    false
                )
                explosionBitmaps.add(bitmap)
            }
        }

        val fireBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.firesheet5x5)
        val firePartImgSizeX = fireBitmap.width / 5 // 1920/8 = 240;
        val firePartImgSizeY = fireBitmap.height / 5 // 1440 / 6 = 240;
        Log.d("TAG_1", "firePartImgSizeY: $firePartImgSizeY")
        for (row in 0..4) {
            for (colm in 0..4) {
                var bitmap = Bitmap.createBitmap(
                    fireBitmap,
                    colm * firePartImgSizeX,
                    row * firePartImgSizeY,
                    firePartImgSizeX,
                    firePartImgSizeY
                )
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    fireFrameWidth,
                    fireFrameHeight,
                    false
                )
                fireBitmaps.add(bitmap)
            }
        }

        val starBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.star)
        val starPartImgSizeX = starBitmap.width / 6 // 1920/8 = 240;
        val starPartImgSizeY = starBitmap.height / 5 // 1440 / 6 = 240;
        Log.d("TAG_1", "firePartImgSizeY: $firePartImgSizeY")
        for (row in 0 until 5) {
            for (colm in 0 until 6) {
                var bitmap = Bitmap.createBitmap(
                    starBitmap,
                    colm * starPartImgSizeX,
                    row * starPartImgSizeY,
                    starPartImgSizeX,
                    starPartImgSizeY
                )
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    starFrameWidth,
                    starFrameHeight,
                    false
                )
                starBitmaps.add(bitmap)
            }
        }
        val star2Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.star2)
        val star2PartImgSizeX = star2Bitmap.width / 5
        val star2PartImgSizeY = star2Bitmap.height / 3
        Log.d(
            "TAG_1",
            "star2PartImgSizeX: $star2PartImgSizeX, star2PartImgSizeY: $star2PartImgSizeY"
        )
        for (row in 0 until 3) {
            for (colm in 0 until 5) {
                var bitmap = Bitmap.createBitmap(
                    star2Bitmap,
                    colm * star2PartImgSizeX,
                    row * star2PartImgSizeY,
                    star2PartImgSizeX,
                    star2PartImgSizeY
                )
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    star2FrameWidth,
                    star2FrameHeight,
                    false
                )
                star2Bitmaps.add(bitmap)
            }
        }
        val star3Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.firesheet9)
        val star3PartImgSizeX = star3Bitmap.width / 8
        val star3PartImgSizeY = star3Bitmap.height / 2
        Log.d(
            "TAG_1",
            "star3PartImgSizeX: $star3PartImgSizeX, star3PartImgSizeY: $star3PartImgSizeY"
        )
        for (row in 0 until 2) {
            for (colm in 0 until 8) {
                var bitmap = Bitmap.createBitmap(
                    star3Bitmap,
                    colm * star3PartImgSizeX,
                    row * star3PartImgSizeY,
                    star3PartImgSizeX,
                    star3PartImgSizeY
                )
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    star3FrameWidth,
                    star3FrameHeight,
                    false
                )
                star3Bitmaps.add(bitmap)
            }
        }

        Log.d("TAG_1", "explosion bitmaps size: ${explosionBitmaps.size}")
        Log.d("TAG_1", "fire bitmaps size: ${fireBitmaps.size}")
        Log.d("TAG_1", "star bitmaps size: ${starBitmaps.size}")
        Log.d("TAG_1", "star2 bitmaps size: ${star2Bitmaps.size}")
        Log.d("TAG_1", "star3 bitmaps size: ${star3Bitmaps.size}")
    }

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        when (motionEvent?.action?.and(MotionEvent.ACTION_MASK)) {
            MotionEvent.ACTION_DOWN -> {
                // Set isExplosion
                explosionXPosition = motionEvent.x
                isExplosion = true
            }
            MotionEvent.ACTION_UP -> {
                // Set isExplosion
//                isExplosion = false
            }
        }
        return true
    }

    init {
        initBitmap(context, explosionBitmaps, fireBitmaps, starBitmaps, star2Bitmaps, star3Bitmaps)
    }


    fun control() {
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


}