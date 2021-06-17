package rewheeldev.tappycosmicevasion.mappers

import rewheeldev.tappycosmicevasion.model.PlayerShip
import rewheeldev.tappycosmicevasion.model.PlayerShipDrawInfo
import javax.inject.Inject

class PlayerMapper @Inject constructor() {
    fun mapPlayerShipInPlayerShipDrawInfo(player: PlayerShip): PlayerShipDrawInfo {
        return PlayerShipDrawInfo(
            player.shipImg,
            player.x,
            player.y,
            player.fireImg,
            player.fireX,
            player.fireY
        )
    }
}