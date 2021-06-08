package ua.yandex.jere184.c4tappydefender.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dagger.android.AndroidInjection
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.databinding.ActivityMainBinding
import ua.yandex.jere184.c4tappydefender.logging.logD
import ua.yandex.jere184.c4tappydefender.logging.logE
import ua.yandex.jere184.c4tappydefender.util.Public
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var myTask: MyTask? = null

    private var bindingImpl: ActivityMainBinding? = null
    private val binding get() = bindingImpl!!

    var imgViewShips: ImageView? = null
    var payerShipIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        bindingImpl = ActivityMainBinding.inflate(layoutInflater)
        log("MainActivity . onCreate")
        setContentView(binding.root)
        hideSystemUI()
        try {
            //region сохраняем размер экрана
            Public.screanSize = Point()
            val display = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) display else windowManager.defaultDisplay
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) windowManager.currentWindowMetrics else display?.getSize(Public.screanSize)

            //endregion
        } catch (ex: Exception) {
            Log.e(
                Public.myLogcatTAG,
                "|||onCreate. new c_Public(this) Exception=" + ex.message + "|||"
            )
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.mainConteiner).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.mainConteiner).show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
    }
    override fun onClick(v: View) {
        if (v.id == R.id.img_btn_right || v.id == imgViewShips!!.id || v.id == R.id.img_btn_left) {
            // смена изображения при нажатии на правую кнопку кнопку
            payerShipIndex++
            if (payerShipIndex > 2) {
                payerShipIndex = 0
            }

            val payerShipID: Int = when (payerShipIndex) {
                0 -> {
                    R.drawable.spaceship_1
                }
                1 -> {
                    R.drawable.spaceship
                }
                else -> {
                    R.drawable.spaceship_2
                }
            }
            imgViewShips!!.setImageBitmap(
                Public.scaleBitmap(
                    BitmapFactory.decodeResource(Public.context!!.resources, payerShipID),
                    3.toByte()
                )
            )
        }
        if (v.id == R.id.tv_nameGlobalRecord) {
            myTask = MyTask()
            myTask!!.execute()
        }
        if (v.id == R.id.mButtonStart) {
            Public.playerShipType = payerShipIndex.toByte()
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
        /*  try {
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
          }*/
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

    private fun log(throwable: Throwable, msg: String = "") {
        logE(throwable, msg)
    }

    private fun log(msg: String = "") {
        logD(msg)
    }

    companion object {
        private const val SAVED_TEXT = "nick_name"
        private const val EXTRA_COUNT_FOR_SHIP = "c4tappydefender.t_payerShipIndex"
    }
}