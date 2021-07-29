package com.example.feature_game.mapper

import com.example.feature_game.model.PlayerShip
import com.example.model.PlayerShipDrawInfo
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