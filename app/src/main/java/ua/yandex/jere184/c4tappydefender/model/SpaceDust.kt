package ua.yandex.jere184.c4tappydefender.model

import ua.yandex.jere184.c4tappydefender.util.Public

class SpaceDust(screenX: Short, screenY: Short) {
    var x: Short
    var y: Short
    private var speed: Short
    var counter: Byte = 0
    var downLight = true

    //Detect dust leaving the screen
    private val maxX: Short
    private val maxY: Short
    private val minX: Short
    private val minY: Short
    fun update(playerSpeed: Float) {
        //Speed up when the player does
        x = (x - playerSpeed.toInt().toShort()).toShort()
        x = (x - speed).toShort()
        //respawn space dust
        if (x < 0) {
            x = maxX
            y = Public.random.nextInt(maxY.toInt()).toShort()
            speed = Public.random.nextInt(15).toShort()
        }
    }

    init {
        counter = Public.random.nextInt(110).toByte()
        maxX = screenX
        maxY = screenY
        minX = 0
        minY = 0

        //Set a speed between 0 and 9
        speed = Public.random.nextInt(10).toShort()

        //Set the starting coordinates
        x = Public.random.nextInt(maxX.toInt()).toShort()
        y = Public.random.nextInt(maxY.toInt()).toShort()
    }
}