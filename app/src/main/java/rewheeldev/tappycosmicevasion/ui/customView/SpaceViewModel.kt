package rewheeldev.tappycosmicevasion.ui.customView

import androidx.lifecycle.ViewModel
import rewheeldev.tappycosmicevasion.util.GameStatus
import javax.inject.Inject

class SpaceViewModel @Inject constructor() : ViewModel() {
    var gameStatus: GameStatus = GameStatus.NOT_START

}