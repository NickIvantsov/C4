package rewheeldev.tappycosmicevasion.ui.customView

import androidx.lifecycle.ViewModel
import rewheeldev.tappycosmicevasion.util.FIRST_LEVEL
import rewheeldev.tappycosmicevasion.util.GameStatus
import rewheeldev.tappycosmicevasion.util.NEW_LEVEL
import javax.inject.Inject

class SpaceViewModel @Inject constructor() : ViewModel() {
    var gameStatus: GameStatus = GameStatus.NOT_START
    var level: Int = FIRST_LEVEL

    fun upNewLevel(): Int {
        level += NEW_LEVEL
        return level
    }
}