package ua.yandex.jere184.c4tappydefender;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

  private TDView gameView;
  Intent intent;
  public static int tmpCount;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    gameView = new TDView(this);
    setContentView(gameView);

    String distance = String.valueOf(gameView._distance);
    String name = "distance";
    intent = getIntent();
    tmpCount = intent.getIntExtra("c4tappydefender.t_payerShipIndex",0);

        /*ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_DISTANCE, distance);*/
  }

  @Override
  public void onPause() {
    super.onPause();
    gameView.pause();
  }

  @Override
  public void onResume() {
    super.onResume();
    gameView.resume();
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {

      finish();
      return true;
    }
    return false;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
  public void onClickEditText(View view) {
  }
}
