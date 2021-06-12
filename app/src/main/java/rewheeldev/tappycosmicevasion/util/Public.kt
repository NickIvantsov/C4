package rewheeldev.tappycosmicevasion.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.net.wifi.WifiManager
import rewheeldev.tappycosmicevasion.model.EnemyShip.Companion.initBitmap
import java.util.*

class Public /*публичный класс со статическими свойствами (которые понадобятся почти по всех проэктах)*/(
    context: Context?
) {
    companion object {
        //endregion
        lateinit var playerName: String
        var wifiSN: String? = null
        var myLogcatTAG: String = ""
        var context: Context? = null
        lateinit var random: Random
        var screanSize: Point? = null
        var playerShipType: Byte = 0

        //уменьшаем изображение по размеру
        fun scaleBitmap(inBitmap: Bitmap, multipler: Byte): Bitmap {
            val newHeight = (screanSize!!.x / 70 * multipler).toFloat()
            val oldHeight = inBitmap.height.toFloat()
            val oldWidth = inBitmap.width.toFloat()
            val r1 = (newHeight - oldHeight) / (oldHeight / 100)
            val r2 = oldWidth + oldWidth / 100 * r1
            return Bitmap.createScaledBitmap(inBitmap, r2.toInt(), newHeight.toInt(), false)
        }
    }

    //region constructor
    init {
        Companion.context = context
        myLogcatTAG = "myOwnTAG"
        //region получаем от системы MAC WIFI
        val wm = Companion.context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiSN = wm.connectionInfo.macAddress
        //endregion
        playerName = "name"
        random = Random()
        initBitmap()
    }
}