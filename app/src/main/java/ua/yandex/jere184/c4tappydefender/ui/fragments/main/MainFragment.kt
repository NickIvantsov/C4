package ua.yandex.jere184.c4tappydefender.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import ua.yandex.jere184.c4tappydefender.databinding.MainFragmentBinding
import ua.yandex.jere184.c4tappydefender.di.SAVED_TEXT
import ua.yandex.jere184.c4tappydefender.model.User
import ua.yandex.jere184.c4tappydefender.util.Public
import ua.yandex.jere184.c4tappydefender.util.toEditable
import javax.inject.Inject

class MainFragment : Fragment() {

    private var bindingImpl: MainFragmentBinding? = null
    private val binding get() = bindingImpl!!

    @Inject
    lateinit var viewModel: MainViewModel
    private val userDataObserver = Observer<User> { userData ->
        userData.nickName?.let {
            Public.playerName = it
            setUserNickName(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingImpl = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserDataLiveData().observe(viewLifecycleOwner, userDataObserver)
        viewModel.readUserData(SAVED_TEXT)
        /*try {

            //region сохраняем размер экрана

            Public.screanSize = Point()
            val display = windowManager.defaultDisplay
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
        imgBtnRight!!.setOnClickListener(this)*/
    }

    private fun setUserNickName(name: String) {
        binding.etNickName.text = name.toEditable()
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}