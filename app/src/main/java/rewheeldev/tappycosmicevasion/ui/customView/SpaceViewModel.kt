package rewheeldev.tappycosmicevasion.ui.customView

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import androidx.lifecycle.ViewModel
import rewheeldev.tappycosmicevasion.mappers.PlayerMapper
import rewheeldev.tappycosmicevasion.model.Meteorite
import rewheeldev.tappycosmicevasion.model.PlayerShip
import rewheeldev.tappycosmicevasion.model.PlayerShipDrawInfo
import rewheeldev.tappycosmicevasion.model.SpaceDust
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import rewheeldev.tappycosmicevasion.repository.ISpaceDustRepository
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.sound.IPlaySoundManager
import com.example.core_utils.util.logging.FIRST_LEVEL
import com.example.core_utils.util.logging.GameStatus
import com.example.core_utils.util.logging.NEW_LEVEL
import com.example.core_utils.util.logging.SoundName
import java.util.*
import javax.inject.Inject

class SpaceViewModel @Inject constructor(
    private val playerMapper: PlayerMapper,
    private val userRecordRepository: IUserRecordRepository,
    private val meteoriteRepository: IMeteoriteRepository,
    private val spaceDustRepository: ISpaceDustRepository,
    private val playSoundManager: IPlaySoundManager
) : ViewModel() {
    var gameStatus: com.example.core_utils.util.logging.GameStatus = com.example.core_utils.util.logging.GameStatus.NOT_START
    var level: Int = com.example.core_utils.util.logging.FIRST_LEVEL

    lateinit var player: PlayerShip

    var timeStarted: Long = 0

    fun getPlayerShipDrawInfo(): PlayerShipDrawInfo =
        playerMapper.mapPlayerShipInPlayerShipDrawInfo(player)

    private fun upNewLevel(): Int {
        level += com.example.core_utils.util.logging.NEW_LEVEL
        return level
    }

    fun startGame(
        context: Context,
        screenX: Int,
        screenY: Int,
        playerShipType: Int,
        random: Random,
        screenSize: Point,
    ) {

        player = PlayerShip(context.applicationContext, screenSize, playerShipType)
        meteoriteRepository.deleteAllMeteorite()
        val meteorite = createNewMeteorite(
            screenX,
            screenY,
            random,
            screenSize,
            meteoriteRepository
        )
        meteoriteRepository.addMeteorite(meteorite)
        //soundPool.play(start,1,1,0,10,1);
        val numSpecs: Short = 100
        spaceDustRepository.deleteAll()
        for (i in 0 until numSpecs) {
            val spec = SpaceDust(screenX, screenY, random)
            spaceDustRepository.add(spec)
        }
        SpaceView.distance = 0f
        SpaceView.timeTaken = 0
        timeStarted = System.currentTimeMillis()
        gameStatus = com.example.core_utils.util.logging.GameStatus.PLAYING
    }

    fun reStartGame(
        screenX: Int,
        screenY: Int,
        random: Random,
        screenSize: Point,
    ) {
        SpaceView.distance = 0f
        level = com.example.core_utils.util.logging.FIRST_LEVEL
        SpaceView.timeTaken = 0
        timeStarted = System.currentTimeMillis()
        gameStatus = com.example.core_utils.util.logging.GameStatus.PLAYING
        player.reInit()
        meteoriteRepository.deleteAllMeteorite()
        val meteorite = createNewMeteorite(
            screenX,
            screenY,
            random,
            screenSize,
            meteoriteRepository
        )
        meteoriteRepository.addMeteorite(meteorite)
    }

    fun startNextLevel(
        screenX: Int,
        screenY: Int,
        random: Random,
        screenSize: Point,
    ) {
        upNewLevel()

        meteoriteRepository.addMeteorite(
            createNewMeteorite(
                screenX,
                screenY,
                random,
                screenSize,
                meteoriteRepository
            )
        )
    }

    private fun createNewMeteorite(
        maxX: Int,
        maxY: Int,
        random: Random,
        screenSize: Point,
        meteoriteRepository: IMeteoriteRepository
    ): Meteorite {
        return Meteorite.createNewMeteorite(
            maxX,
            maxY,
            random,
            screenSize,
            meteoriteRepository
        )
    }

    suspend fun playSound(soundName: com.example.core_utils.util.logging.SoundName) {
        playSoundManager.play(soundName)
    }

    fun getSpaceDustOpacity(counter: Int): Int {
        var opacity = counter * 5
        when {
            opacity > 255 -> opacity = 255
            opacity < 50 -> opacity = 50
        }
        return opacity
    }

    fun getCurrentSpaceDustColor(opacity: Int, counter: Int): Int {
        return when {
            counter % SpaceView.NUMBER_OF_FRAMES > 6 -> {
                // всего 30 кадров с 6 по 30 работает обычное затухание прозрачности белой звезды
                Color.argb(
                    opacity,
                    255,
                    255,
                    255
                )
            }
            counter % SpaceView.NUMBER_OF_FRAMES > 3 -> {
                // с 3 по 6 ярко светится синим
                Color.argb(
                    255,
                    0,
                    0,
                    255
                )
            }
            else -> {
                // с 0 по 3 ярко светится (типо ораньжевым)
                Color.argb(255, 200, 100, 0)
            }
        }
    }
}