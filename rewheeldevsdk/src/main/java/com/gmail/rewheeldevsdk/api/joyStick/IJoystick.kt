package com.gmail.rewheeldevsdk.api.joyStick

import android.graphics.Canvas

interface IJoystick {
    var isPressed:Boolean
    val actuatorX:Double
    val actuatorY:Double
    fun draw(canvas: Canvas)
    fun update()
    fun setActuator(touchPositionX: Double, touchPositionY: Double)
    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean
    fun resetActuator()
}