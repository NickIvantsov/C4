package rewheeldev.tappycosmicevasion.model

import java.util.*

class SpaceDust(
    screenX: Short,
    screenY: Short,
    private val random: Random
) {
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
            y = random.nextInt(maxY.toInt()).toShort()
            speed = random.nextInt(15).toShort()
        }
    }

    init {
        counter = random.nextInt(110).toByte()
        maxX = screenX
        maxY = screenY
        minX = 0
        minY = 0

        //Set a speed between 0 and 9
        speed = random.nextInt(10).toShort()

        //Set the starting coordinates
        x = random.nextInt(maxX.toInt()).toShort()
        y = random.nextInt(maxY.toInt()).toShort()
    }
}