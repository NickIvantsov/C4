package rewheeldev.tappycosmicevasion.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.core_utils.util.logging.hideSystemUI
import dagger.android.AndroidInjection
import rewheeldev.tappycosmicevasion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var bindingImpl: ActivityMainBinding? = null
    private val binding get() = bindingImpl!!

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        bindingImpl = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        hideSystemUI(window, binding.mainConteiner)
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingImpl = null
    }
}