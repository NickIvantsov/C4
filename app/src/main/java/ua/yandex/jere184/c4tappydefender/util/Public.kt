package ua.yandex.jere184.c4tappydefender.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.wifi.WifiManager;

import java.util.Random;

import ua.yandex.jere184.c4tappydefender.net.NativeWorkWithData;
import ua.yandex.jere184.c4tappydefender.model.EnemyShip;

public class Public /*публичный класс со статическими свойствами (которые понадобятся почти по всех проэктах)*/ {
    //region constructor
    public Public(Context context) {
        Public.context = context;
        myLogcatTAG = "myOwnTAG";
        //region получаем от системы MAC WIFI
        WifiManager wm = (WifiManager) Public.context.getSystemService(Public.context.WIFI_SERVICE);
        Public.wifiSN = wm.getConnectionInfo().getMacAddress();
        //endregion
        playerName = "name";
        random = new Random();
        data = new NativeWorkWithData();
        EnemyShip.f_initBitmap();
    }
    //endregion

    public static String playerName;
    public static String wifiSN;
    public static String myLogcatTAG;
    public static Context context;
    public static Random random;
    public static Point screanSize;

    public static NativeWorkWithData data;

    public static byte playerShipType = 0;

    //уменьшаем изображение по размеру
    public static Bitmap scaleBitmap(Bitmap inBitmap, byte multipler) {
        float newHeight = (Public.screanSize.x / 70) * multipler;
        float oldHeight = inBitmap.getHeight();
        float oldWidth = inBitmap.getWidth();
        float r1 = ((newHeight - oldHeight) / (oldHeight / 100));
        float r2 = (oldWidth + ((oldWidth / 100) * r1));
        return Bitmap.createScaledBitmap(inBitmap, (int) r2, (int) newHeight, false);
    }
}

