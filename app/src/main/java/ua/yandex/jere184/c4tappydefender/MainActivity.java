package ua.yandex.jere184.c4tappydefender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  EditText editText;
  c_myTask _myTask;
  //создание эл для сохранения ника в приложении
  SharedPreferences sPref;
  SharedPreferences.Editor ed;
  private static final String SAVED_TEXT = "nick_name";

  ImageButton img_btn_left, img_btn_right;
  ImageView img_view_ships;
  int t_payerShipIndex = 0;
  private static final String EXTRA_COUNT_FOR_SHIP = "c4tappydefender.t_payerShipIndex";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(c_Public._myLogcatTAG, "MainActivity . onCreate");
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    setContentView(R.layout.activity_main);
    try {
      new c_Public(this);// инициализируем
      //region сохраняем размер экрана
      Display display = getWindowManager().getDefaultDisplay();
      c_Public._screanSize = new Point();
      display.getSize(c_Public._screanSize);
      //endregion
    } catch (Exception ex) {
      Log.e(c_Public._myLogcatTAG, "|||onCreate. new c_Public(this) Exception=" + ex.getMessage() + "|||");
    }

    c_Public._data.tv_localData = (TextView) findViewById(R.id.tv_my_firstPlace);
    c_Public._data.tv_servData = (TextView) findViewById(R.id.tv_firstPlace);

    img_view_ships = findViewById(R.id.img_view_ships);
    img_view_ships.setImageBitmap(
            c_Public.f_scaleBitmap(BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship_1)
                    , (byte) 3));
    img_btn_left = findViewById(R.id.img_btn_left);
    img_btn_right = findViewById(R.id.img_btn_right);

    editText = (EditText) findViewById(R.id.editText);
    //editText.setText(c_Public._PlayerName);
    sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE);
    String savedText = sPref.getString(SAVED_TEXT, "");
    editText.setText(savedText);

    final Button buttonPlay = (Button) findViewById(R.id.mButtonStart);
    buttonPlay.setOnClickListener(this);
    img_btn_left.setOnClickListener(this);
    img_btn_right.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.img_btn_right || v.getId() == img_view_ships.getId() || v.getId() == R.id.img_btn_left) {
      // смена изображения при нажатии на правую кнопку кнопку
      t_payerShipIndex++;
      if (t_payerShipIndex > 2) {
        t_payerShipIndex = 0;
      }

    /*//тоже рабочий вариант
    Bitmap t_shipBitmap;
    if (t_payerShipIndex == 0) {
      t_shipBitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship_1);
    } else if (t_payerShipIndex == 1) {
      t_shipBitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship);
    } else {
      t_shipBitmap = BitmapFactory.decodeResource(c_Public._context.getResources(), R.drawable.spaceship_2);
    }
    img_view_ships.setImageBitmap(c_Public.f_scaleBitmap(t_shipBitmap, (byte) 3));//t_shipBitmap);
*/
      int t_payerShipID;
      if (t_payerShipIndex == 0) {
        t_payerShipID = R.drawable.spaceship_1;
      } else if (t_payerShipIndex == 1) {
        t_payerShipID = R.drawable.spaceship;
      } else {
        t_payerShipID = R.drawable.spaceship_2;
      }
      img_view_ships.setImageBitmap(
              c_Public.f_scaleBitmap(BitmapFactory.decodeResource(c_Public._context.getResources(), t_payerShipID)
                      , (byte) 3));
    }

    if (v.getId() == R.id.tv_nameGlobalRecord) {
      //region v1
//      c_Public._data.tv_servData.setText("");
//      c_Public._data.f_getTopResultsFromDB();
//      c_Public._data.f_readLocalDB();
      //endregion
      _myTask = new c_myTask();
      _myTask.execute();
    }
    if (v.getId() == R.id.mButtonStart) {
      c_Public.t_playerShipType = (byte) t_payerShipIndex;
      c_Public._PlayerName = editText.getText().toString();
      Intent i = new Intent(MainActivity.this, GameActivity.class);
      i.putExtra(EXTRA_COUNT_FOR_SHIP, t_payerShipIndex);
      Log.d(c_Public._myLogcatTAG, i + "");
      startActivity(i);
    }

  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      finish();
      return true;
    }
    return false;
  }

  @Override
  public void onResume() {
    super.onResume();
    try {
      c_Public._data.f_readLocalDB();
      c_Public._data.f_getTopResultsFromDB();
    } catch (Exception ex) {
      Log.e(c_Public._myLogcatTAG, "|||onResume. c_Public._data.f_getTopResultsFromDB() Exception=" + ex.getMessage() + "|||");
    }
    if (editText.length() > 5) {
      sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE);
      ed = sPref.edit();
      ed.putString(SAVED_TEXT, editText.getText().toString());
      ed.apply();
    } else {
      editText.setText("Simple name");
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE);
    ed = sPref.edit();
    ed.putString(SAVED_TEXT, editText.getText().toString());
    ed.apply();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE);
    ed = sPref.edit();
    ed.putString(SAVED_TEXT, editText.getText().toString());
    ed.apply();

  }

  class c_myTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      c_Public._data.tv_servData.setText("");
      c_Public._data.tv_servData.setText("Processing request ...");
    }

    @Override
    protected Void doInBackground(Void... voids) {
      c_Public._data.f_getTopResultsFromDB();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {

      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      c_Public._data.tv_servData.setText("");
      c_Public._data.f_readLocalDB();
    }
  }

}



