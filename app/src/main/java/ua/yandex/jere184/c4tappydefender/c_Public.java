package ua.yandex.jere184.c4tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Display;

import java.util.Random;

public class c_Public /*публичный класс со статическими свойствами (которые понадобятся почти по всех проэктах)*/ {
  //region constructor
  public c_Public(Context context) {
    _context = context;
    _myLogcatTAG = "myOwnTAG";
    //region получаем от системы MAC WIFI
    WifiManager wm = (WifiManager) _context.getSystemService(_context.WIFI_SERVICE);
    c_Public._wifiSN = wm.getConnectionInfo().getMacAddress();
    //endregion
    _PlayerName = "name";
    _random = new Random();
    _data = new c_nativWorkWithData();
    EnemyShip.f_initBitmap();
  }
  //endregion

  public static String _PlayerName;
  public static String _wifiSN;
  public static String _myLogcatTAG;
  public static Context _context;
  public static Random _random;
  public static Point _screanSize;

  public static c_nativWorkWithData _data;

  public static byte t_playerShipType = 0;

//уменьшаем изображение по размеру
  public static Bitmap f_scaleBitmap(Bitmap inBitmap, byte multipler) {
    float newHeight = (c_Public._screanSize.x / 70) * multipler;
    float oldHeight = inBitmap.getHeight();
    float oldWidth = inBitmap.getWidth();
    float r1 = ((newHeight - oldHeight) / (oldHeight / 100));
    float r2 = (oldWidth + ((oldWidth / 100) * r1));
    return Bitmap.createScaledBitmap(inBitmap, (int) r2,(int) newHeight, false);
  }
}
class c_myLog {
  //Кто леньтяй ? тот кто пишет класс лог вместо стадартного )))
  short counter = 0;
  String Logcat_TAG = null;
  String prevStr = null;
  String postStr = null;
  public c_myLog(String Logcat_TAG, String prevStr, String postStr) {
    this.Logcat_TAG = Logcat_TAG;
    this.prevStr = prevStr;
    this.postStr = postStr;
  }
  public void L() {
    String msg = "";
    if (prevStr != null) msg = msg + prevStr;
    counter++;
    msg = msg + "# " + counter + " #";
    if (postStr != null) msg = msg + postStr;

    Log.d(Logcat_TAG, msg);
  }
  public void LT() {
    String msg = "";
    if (prevStr != null) msg = msg + prevStr;
    counter++;
    msg = msg + "# " + counter + " #";
    msg = msg + "||time=" + System.currentTimeMillis() + "||";
    if (postStr != null) msg = msg + postStr;

    Log.d(Logcat_TAG, msg);
  }
  public void LT(long startTime) {
    String msg = "";
    if (prevStr != null) msg = msg + prevStr;
    counter++;
    msg = msg + "# " + counter + " #";
    msg = msg + "||time=" + (System.currentTimeMillis() - startTime) + "||";
    if (postStr != null) msg = msg + postStr;

    Log.d(Logcat_TAG, msg);
  }
}