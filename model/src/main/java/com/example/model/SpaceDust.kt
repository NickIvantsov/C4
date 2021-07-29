package com.example.model

import java.util.*

class SpaceDust(
    screenX: Int,
    screenY: Int,
    private val random: Random
) {
    var x: Int
    var y: Int
    private var speed = 0
    var counter = 0
    var downLight = true

    //Detect dust leaving the screen
    private val maxX: Int
    private val maxY: Int
    private val minX: Int
    private val minY: Int

    fun update(playerSpeed: Float) {
        //Speed up when the player does
        x = (x - playerSpeed.toInt().toShort())
        x = (x - speed)
        //respawn space dust
        if (x < 0) {
            x = maxX
            y = random.nextInt(maxY)
            speed = random.nextInt(15)
        }
    }

    init {
        counter = random.nextInt(110)
        maxX = screenX
        maxY = screenY
        minX = 0
        minY = 0

        //Set a speed between 0 and 9
        val randomSpeed = random.nextInt(10)
        speed = if (randomSpeed == 0) 1 else randomSpeed

        //Set the starting coordinates
        x = random.nextInt(maxX)
        y = random.nextInt(maxY)
    }
}