package rewheeldev.tappycosmicevasion.ui

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import rewheeldev.tappycosmicevasion.databinding.ActivityMainBinding
import rewheeldev.tappycosmicevasion.logging.logD
import rewheeldev.tappycosmicevasion.logging.logE
import rewheeldev.tappycosmicevasion.util.Public
import rewheeldev.tappycosmicevasion.util.hideSystemUI

class MainActivity : AppCompatActivity() {

    private var bindingImpl: ActivityMainBinding? = null
    private val binding get() = bindingImpl!!
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        bindingImpl = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            //region сохраняем размер экрана
            Public.screanSize = Point()
            val display =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) display else windowManager.defaultDisplay
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) windowManager.currentWindowMetrics else display?.getSize(
                Public.screanSize
            )

            //endregion
        } catch (ex: Exception) {
            log(ex, "|||onCreate. new c_Public(this) Exception=" + ex.message + "|||")
        }
    }


   /* override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return false
    }*/

    override fun onResume() {
        hideSystemUI(window, binding.mainConteiner)
        super.onResume()
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