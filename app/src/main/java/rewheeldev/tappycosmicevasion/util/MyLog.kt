package rewheeldev.tappycosmicevasion.util

import android.util.Log

internal class MyLog(
    private val logcatTag: String?,
    private val prevStr: String?,
    private val postStr: String?
) {
    var counter: Short = 0
    fun L() {
        var msg = ""
        if (prevStr != null) msg += prevStr
        counter++
        msg = "$msg# $counter #"
        if (postStr != null) msg += postStr
        Log.d(logcatTag, msg)
    }

    fun LT() {
        var msg = ""
        if (prevStr != null) msg += prevStr
        counter++
        msg = "$msg# $counter #"
        msg = msg + "||time=" + System.currentTimeMillis() + "||"
        if (postStr != null) msg += postStr
        Log.d(logcatTag, msg)
    }

    fun LT(startTime: Long) {
        var msg = ""
        if (prevStr != null) msg += prevStr
        counter++
        msg = "$msg# $counter #"
        msg = msg + "||time=" + (System.currentTimeMillis() - startTime) + "||"
        if (postStr != null) msg += postStr
        Log.d(logcatTag, msg)
    }
}