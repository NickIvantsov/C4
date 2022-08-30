package com.example.feature_game.ui.screens

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.feature_game.model.GameViewParams
import com.example.feature_game.tmp.SpaceView

/** `GameView` is a class that extends `View` and is used to display the game.
 *
 * @author Mykola Ivantsov
 * @since 31 August 2022
 */
class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) :
    LinearLayout(context, attrs, defStyle) {
    private lateinit var spaceView: SpaceView
    /* A property that is used to set the debug mode for the `SpaceView` class. */
    var debugEnable: Boolean = false
        set(value) {
            field = value
            spaceView.debugEnable = value
        }
        get() {
            return spaceView.debugEnable
        }


    /**
     * > The function initializes the game view by creating a new instance of the `SpaceView` class and
     * adding it to the `GameView` class
     *
     * @param gameViewParams GameViewParams
     */
    fun initialize(gameViewParams: GameViewParams) {

        with(gameViewParams) {
            spaceView = SpaceView(
                context,
                userRecordRepository = userRecordRepository,
                random = random,
                screenSize = screenSize,
                playerShipType = playerShipType,
                meteoriteRepository = meteoriteRepository,
                spaceDustInteractor = spaceDustInteractor,
                spaceViewModel = spaceViewModel
            )
        }
        addView(spaceView)
    }

    /**
     * "Set the number of frames to skip between each frame drawn to the screen."
     *
     * The function is called from the main activity's onCreate() method
     *
     * @param fps The number of frames per second to display.
     */
    fun setFPSDivider(fps: Int) {
        spaceView.fpsDivider = fps.toLong()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        spaceView.setScreenY(MeasureSpec.getSize(heightMeasureSpec))
        spaceView.setScreenX(MeasureSpec.getSize(widthMeasureSpec))
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * The function `pause()` is called when the user presses the back button on their device
     */
    fun pause() {
        spaceView.pause()
    }

    /**
     * Resumes the game
     */
    fun resume() {
        spaceView.resume()
    }
}