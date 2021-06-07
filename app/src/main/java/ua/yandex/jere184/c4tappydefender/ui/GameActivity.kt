package ua.yandex.jere184.c4tappydefender.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private var gameView: TDView? = null
    var mIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        gameView = TDView(this)
        setContentView(gameView)
        val distance = TDView._distance.toString()
        val name = "distance"
        mIntent = intent
        tmpCount = mIntent!!.getIntExtra("c4tappydefender.t_payerShipIndex", 0)

        /*ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_DISTANCE, distance);*/
    }

    public override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    public override fun onResume() {
        super.onResume()
        gameView!!.resume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return false
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    fun onClickEditText(view: View?) {}

    companion object {
        var tmpCount = 0
    }
}