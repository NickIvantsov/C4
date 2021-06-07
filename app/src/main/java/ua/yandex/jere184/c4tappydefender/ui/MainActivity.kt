package ua.yandex.jere184.c4tappydefender.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.logging.logD
import ua.yandex.jere184.c4tappydefender.logging.logE
import ua.yandex.jere184.c4tappydefender.util.Public
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var editText: EditText? = null
    private var myTask: MyTask? = null

    //создание эл для сохранения ника в приложении
    var sPref: SharedPreferences? = null
    var ed: SharedPreferences.Editor? = null
    var imgBtnLeft: ImageButton? = null
    var imgBtnRight: ImageButton? = null
    var imgViewShips: ImageView? = null
    var payerShipIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        log("MainActivity . onCreate")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_main)
        try {
            Public(this) // инициализируем
            //region сохраняем размер экрана
            val display = windowManager.defaultDisplay
            Public.screanSize = Point()
            display.getSize(Public.screanSize)
            //endregion
        } catch (ex: Exception) {
            Log.e(
                Public.myLogcatTAG,
                "|||onCreate. new c_Public(this) Exception=" + ex.message + "|||"
            )
        }
        Public.data.tvLocalData = findViewById<View>(R.id.tv_my_firstPlace) as TextView
        Public.data.tvServData = findViewById<View>(R.id.tv_firstPlace) as TextView
        imgViewShips = findViewById(R.id.img_view_ships)
        imgViewShips!!.setImageBitmap(
            Public.scaleBitmap(
                BitmapFactory.decodeResource(Public.context!!.resources, R.drawable.spaceship_1),
                3.toByte()
            )
        )
        imgBtnLeft = findViewById(R.id.img_btn_left)
        imgBtnRight = findViewById(R.id.img_btn_right)
        editText = findViewById<View>(R.id.editText) as EditText
        //editText.setText(c_Public._PlayerName);
        sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE)
        val savedText = sPref!!.getString(SAVED_TEXT, "")
        editText!!.setText(savedText)
        val buttonPlay = findViewById<View>(R.id.mButtonStart) as Button
        buttonPlay.setOnClickListener(this)
        imgBtnLeft!!.setOnClickListener(this)
        imgBtnRight!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.img_btn_right || v.id == imgViewShips!!.id || v.id == R.id.img_btn_left) {
            // смена изображения при нажатии на правую кнопку кнопку
            payerShipIndex++
            if (payerShipIndex > 2) {
                payerShipIndex = 0
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
            val t_payerShipID: Int
            t_payerShipID = if (payerShipIndex == 0) {
                R.drawable.spaceship_1
            } else if (payerShipIndex == 1) {
                R.drawable.spaceship
            } else {
                R.drawable.spaceship_2
            }
            imgViewShips!!.setImageBitmap(
                Public.scaleBitmap(
                    BitmapFactory.decodeResource(Public.context!!.resources, t_payerShipID),
                    3.toByte()
                )
            )
        }
        if (v.id == R.id.tv_nameGlobalRecord) {
            //region v1
//      c_Public._data.tv_servData.setText("");
//      c_Public._data.f_getTopResultsFromDB();
//      c_Public._data.f_readLocalDB();
            //endregion
            myTask = MyTask()
            myTask!!.execute()
        }
        if (v.id == R.id.mButtonStart) {
            Public.playerShipType = payerShipIndex.toByte()
            Public.playerName = editText!!.text.toString()
            val i = Intent(this@MainActivity, GameActivity::class.java)
            i.putExtra(EXTRA_COUNT_FOR_SHIP, payerShipIndex)
            log(i.toString() + "")
            startActivity(i)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return false
    }

    public override fun onResume() {
        super.onResume()
        try {
            Public.data.readLocalDB()
            Public.data.topResultsFromDB
        } catch (ex: Exception) {
            Log.e(
                Public.myLogcatTAG,
                "|||onResume. c_Public._data.f_getTopResultsFromDB() Exception=" + ex.message + "|||"
            )
        }
        if (editText!!.length() > 5) {
            sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE)
            ed = sPref!!.edit()
            ed!!.putString(SAVED_TEXT, editText!!.text.toString())
            ed!!.apply()
        } else {
            editText!!.setText("Simple name")
        }
    }

    override fun onStop() {
        super.onStop()
        sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE)
        ed = sPref!!.edit()
        ed!!.putString(SAVED_TEXT, editText!!.text.toString())
        ed!!.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        sPref = getSharedPreferences(SAVED_TEXT, MODE_PRIVATE)
        ed = sPref!!.edit()
        ed!!.putString(SAVED_TEXT, editText!!.text.toString())
        ed!!.apply()
    }

    inner class MyTask : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Public.data.tvServData!!.text = ""
            Public.data.tvServData!!.text = "Processing request ..."
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            Public.data.tvServData!!.text = ""
            Public.data.readLocalDB()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            Public.data.topResultsFromDB
            try {
                TimeUnit.SECONDS.sleep(3)
            } catch (e: InterruptedException) {
                log(e)
            }
            return null
        }
    }

    private fun log(throwable: Throwable,msg:String=""){
        logE(throwable,msg)
    }
    private fun log(msg:String=""){
        logD(msg)
    }
    companion object {
        private const val SAVED_TEXT = "nick_name"
        private const val EXTRA_COUNT_FOR_SHIP = "c4tappydefender.t_payerShipIndex"
    }
}