package com.gmail.rewheeldevsdk.api.collision

import android.graphics.Bitmap
import android.graphics.Rect

/* "This interface is used to define the methods that will be used to determine if two objects have
collided."

The first method, getCurrentFrame(), returns the current frame of the object. This is used to
determine the size of the object */
interface ICollision {
    /**
     * Returns the current frame of the video as a Bitmap.
     */
    fun getCurrentFrame(): Bitmap
    /**
     * It returns a Rect object for frame hit box.
     */
    fun getFrameHitBox(): Rect
}