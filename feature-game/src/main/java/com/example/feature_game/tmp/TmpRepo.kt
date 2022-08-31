package com.example.feature_game.tmp

import android.graphics.Bitmap
import com.gmail.rewheeldevsdk.api.models.CollisionInfo


object CacheValidationImages {
    val meteorites = mutableListOf<CollisionInfo>()
    lateinit var playerShip: CollisionInfo

    fun init(imgArray: List<Bitmap>) {
        imgArray.forEach {
            val collisionInfo = CollisionInfo(it)
            collisionInfo.initializeImageBlocks()
            meteorites.add(collisionInfo)
        }
    }
}