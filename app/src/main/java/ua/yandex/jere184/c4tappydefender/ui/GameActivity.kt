package ua.yandex.jere184.c4tappydefender.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import ua.yandex.jere184.c4tappydefender.repository.IUserRecordRepository
import ua.yandex.jere184.c4tappydefender.util.hideSystemUI
import javax.inject.Inject

class GameActivity : AppCompatActivity() {

    private var gameView: TDView? = null
    var mIntent: Intent? = null

    @Inject
    lateinit var userRecordRepository: IUserRecordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        gameView = TDView(this, userRecordRepository)
        setContentView(gameView)
        mIntent = intent
        tmpCount = mIntent!!.getIntExtra("c4tappydefender.t_payerShipIndex", 0)
    }

    public override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    public override fun onResume() {
        hideSystemUI(window, gameView!!)
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

    companion object {
        var tmpCount = 0
    }
}