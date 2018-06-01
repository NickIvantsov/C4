package ua.yandex.jere184.c4tappydefender;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
  //region объявления
  //c_joystick _joystick;
//    private SharedPreferences prefs;
//    private SharedPreferences.Editor editor;

  //region Добавление звуков v1
  private SoundPool soundPool;
  int start = -1;
  int bump = -1;
  int destroyed = -1;
  int win = -1;
  //endregion
  //region flags
  private boolean gameEnded;
  private byte _level = 1;
  volatile boolean playing;
  //endregion
  //region objects
  private PlayerShip player;
  public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();
  public ArrayList<EnemyShip> enemyList = new ArrayList<EnemyShip>();
  //endregion
  //region Счетчики
  public static float _distance;
  public static long timeTaken;
  private long timeStarted;
  //endregion
  private Context _context;
  private int screenX;
  private int screenY;
  private Paint paint;
  private Canvas canvas;
  private SurfaceHolder ourHolder;
  Thread gameThread = null;
  //region OnTouch
  View.OnTouchListener _OnTouchSpeed;
  //endregion
  //endregion
  public TDView(Context context) {
    super(context);
    this._context = context;
    //region v1 добавление звука
    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    try{
      AssetManager assetManager = context.getAssets();
      AssetFileDescriptor descriptor;

      descriptor = assetManager.openFd("start.ogg");
      start = soundPool.load(descriptor,0);

//      descriptor = assetManager.openFd("win.ogg");
//      win = soundPool.load(descriptor,0);

      descriptor = assetManager.openFd("bump.ogg");
      bump = soundPool.load(descriptor,0);
      descriptor = assetManager.openFd("destroyed.ogg");
      destroyed = soundPool.load(descriptor,0);
    }catch (IOException e){
      Log.d(c_Public._myLogcatTAG,"||| TDView||| error failed to load sound files" );
    }
    //_joystick = new c_joystick();
    screenX = c_Public._screanSize.x;
    screenY = c_Public._screanSize.y;
    ourHolder = getHolder();
    paint = new Paint();
    startGame();
    //region OnTouch
    _OnTouchSpeed = new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        int actionMask = motionEvent.getActionMasked();
        player.touchY = motionEvent.getY();
        int t_startSpeedX = screenX - screenX / 8;

        if (actionMask == MotionEvent.ACTION_MOVE) {
          if (motionEvent.getX() > t_startSpeedX) {
            player.is_touchSpeed = true;
          } else {
            player.is_touchSpeed = false;
          }
        }
        if (actionMask == MotionEvent.ACTION_POINTER_DOWN) {
          if (motionEvent.getX() > t_startSpeedX) {
            player.is_touchSpeed = true;
          } else {
            player.is_touchSpeed = false;
          }
        }
        if (actionMask == MotionEvent.ACTION_DOWN) {
          if (gameEnded) {
            reStartGame();
            return false;
          }
          if (motionEvent.getX() > t_startSpeedX) {
            player.is_touchSpeed = true;
          } else {
            player.is_touchSpeed = false;
          }
        }
        if (actionMask == MotionEvent.ACTION_UP) {
          player.is_touchSpeed = false;

        }
        return true;

        //region Joystick
              /*
                int actionMask = motionEvent.getActionMasked();
                player.touchY = motionEvent.getY();

                if (actionMask == MotionEvent.ACTION_MOVE) {
                    if(_joystick.is_activ) {
                        float t_dist = _joystick._coordDown.f_minus();
                        if (t_dist < 30) {
                        } else if (t_dist< 100) {
                            player.is_touchSpeed = tu;
                        }
                    }
                }
                if (actionMask == MotionEvent.ACTION_POINTER_DOWN) {
                    _joystick.s_newCoordDown( motionEvent.getX(), motionEvent.getY());
                }
                if (actionMask == MotionEvent.ACTION_DOWN) {
                    _joystick.s_newCoordDown( motionEvent.getX(), motionEvent.getY());
                }
                if (actionMask == MotionEvent.ACTION_UP) {
                    _joystick.is_activ = false;
                }
                return true;*/
        //endregion
      }
    };
    this.setOnTouchListener(_OnTouchSpeed);
    //endregion
  }
  @Override
  public void run() {
    while (playing) {
      update();
      draw();
      control();
    }
  }
  private void update() {
    if (gameEnded) return;
    boolean hitDetected = false;
    for (int i = 0; i < enemyList.size(); i++) {
      if (Rect.intersects(player.getHitBox(), enemyList.get(i).getHitBox())) {
        hitDetected = true;
        enemyList.get(i).setX(-350);
      }
    }
    if (hitDetected) {
      soundPool.play(bump,1,1,0,0,1);
      player.f_minusLifes();
      if (player.get_lifes() <= 0)/*количество жизней меньше либо равно нулю */ {
        hitDetected = false;
        gameEnded = true;
        soundPool.play(destroyed,1,1,0,0,1);

        c_Public._data.f_addNewResult(_distance, timeTaken);
      }
    }
    for (SpaceDust sd : dustList) {
      sd.update(player.g_speed());
    }
    player.update();
    for (int i = 0; i < enemyList.size(); i++) {
      enemyList.get(i).update(player.g_speed());
    }
    if (!gameEnded) {
      _distance += (player.g_speed() / 1000.0);
      timeTaken = System.currentTimeMillis() - timeStarted;
    }
    if (_distance >= _level * 5) {
      f_startNextLevel();
    }
  }
  private void f_startNextLevel() {
    _level++;
    enemyList.add(new EnemyShip(screenX, screenY));
  }
  private void draw() {

    if (ourHolder.getSurface().isValid()) {
      canvas = ourHolder.lockCanvas();

      canvas.drawColor(Color.argb(255, 0, 0, 0));

      paint.setColor(Color.argb(255, 255, 255, 255));
      //region draw objects
      if (!gameEnded) {

        //region римуем звезды )
        for (int i = 0; i < dustList.size(); i++) {
          if (dustList.get(i).downLight)
            dustList.get(i).counter++;
          else
            dustList.get(i).counter--;

          if (dustList.get(i).counter < 10) {
            dustList.get(i).downLight = true;
          } else if (dustList.get(i).counter >= 125) {
            dustList.get(i).downLight = false;
          }
          short t_opacity = (short) (dustList.get(i).counter * 5);
          if (t_opacity > 255) t_opacity = 255;
          else if (t_opacity < 50) t_opacity = 50;

          //region мигание синим и ораньжевым цветом
          if (dustList.get(i).counter % 30 > 6)// всего 30 кадров с 6 по 30 работает обычное затухание прозрачности белой звезды
            paint.setColor(Color.argb(t_opacity, 255, 255, 255));
          else if (dustList.get(i).counter % 30 > 3)// с 3 по 6 ярко светится синим
            paint.setColor(Color.argb(255, 0, 0, 255));
          else // с 0 по 3 ярко светится (типо ораньжевым)
            paint.setColor(Color.argb(255, 200, 100, 0));
          //endregion

          canvas.drawPoint(dustList.get(i).x, dustList.get(i).y, paint);
        }
        //endregion

        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawBitmap(player.g_bitmap(), player.x, player.y, paint);

        if (player.is_touchSpeed)
          canvas.drawBitmap(player._fireImg, player.x + player.fire_x, player.y + player.fire_y, paint);

        for (int i = 0; i < enemyList.size(); i++) {
          canvas.drawBitmap(enemyList.get(i).bitmap, enemyList.get(i).x, enemyList.get(i).y, paint);
        }
      }
      //endregion
      if (!gameEnded) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextSize(25);
        canvas.drawText("Level" + _level + "", 10, 20, paint);
        canvas.drawText("Time:" + timeTaken / 1000 + "s", screenX / 2, 20, paint);
        canvas.drawText("Distance:" + ((short) _distance) + "KM", screenX / 3, screenY - 20, paint);
        canvas.drawText("Speed:" + (short) (player.g_speed() * 60) + " KMh", (screenX / 3) * 2, screenY - 20, paint);
        if (player.isReduceShieldStrength <= 0) {
          paint.setTextSize(25);
        } else {
          if (player.isReduceShieldStrength % 10 > 5) {
            paint.setColor(Color.argb(255, 255, 0, 0));
          } else {
            paint.setColor(Color.argb(255, 255, 255, 255));
          }
          paint.setTextSize(25 + player.isReduceShieldStrength);
          player.isReduceShieldStrength--;
        }
        canvas.drawText("Shield:" + player.get_lifes(), 10, screenY - 20, paint);

        if (player.is_touchSpeed) {
          if (dustList.get(0).counter % 14 >= 7)
            paint.setColor(Color.argb(255, 0, 255, 0));
          else
            paint.setColor(Color.argb(255, 255, 255, 255));
        } else
          paint.setColor(Color.argb(100, 255, 255, 255));

        paint.setTextSize(60);
        for (byte i = 1; i < (screenY / 100); i++)
          canvas.drawText(">>>", screenX - screenX / 8, i * 100, paint);

      } else {
        paint.setTextSize(80);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over", screenX / 2, 100, paint);
        paint.setTextSize(25);
        //canvas.drawText("Level" + fastestTime + "s", screenX / 2, 160, paint);
        canvas.drawText("Time:" + (timeTaken / 1000) + "s", screenX / 2, 200, paint);
        canvas.drawText("Distance remaining:" + (short) (_distance) + " KM", screenX / 2, 240, paint);
        paint.setTextSize(80);
        canvas.drawText("Tap to replay!", screenX / 2, 350, paint);
      }

      ourHolder.unlockCanvasAndPost(canvas);
    }

  }
  private void control() {
    try {
      gameThread.sleep(20);
    } catch (InterruptedException e) {

    }
  }
  public void pause() {
    playing = false;
    try {
      gameThread.join();
    } catch (InterruptedException ex) {

    }
  }
  public void resume() {
    playing = true;
    gameThread = new Thread(this);
    gameThread.start();
  }
  private void startGame() {
    player = new PlayerShip();

    enemyList.clear();
    enemyList.add(new EnemyShip(screenX, screenY));
    //soundPool.play(start,1,1,0,10,1);
    short numSpecs = 100;
    dustList.clear();
    for (int i = 0; i < numSpecs; i++) {
      SpaceDust spec = new SpaceDust((short) screenX, (short) screenY);
      dustList.add(spec);
    }

    _distance = 0;
    timeTaken = 0;

    timeStarted = System.currentTimeMillis();
    gameEnded = false;
  }
  private void reStartGame() {
    _distance = 0;
    _level = 1;
    timeTaken = 0;
    timeStarted = System.currentTimeMillis();
    gameEnded = false;
    player.f_reInit();
    enemyList.clear();
    enemyList.add(new EnemyShip(screenX, screenY));
  }
  public float get_distance()/* ???? */ {
    return _distance;
  }

}
/*
class c_joystick {
  c_coord _coordDown;
  boolean is_activ = false;
  public c_joystick() {
  }
  public void s_newCoordDown(float _x, float _y) {
    is_activ = true;
    _coordDown.set(_x, _y);
  }
}*/