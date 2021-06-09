package ua.yandex.jere184.c4tappydefender.ui.fragments.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.yandex.jere184.c4tappydefender.R
import ua.yandex.jere184.c4tappydefender.databinding.MainFragmentBinding
import ua.yandex.jere184.c4tappydefender.db.userRecords.UserRecordEntity
import ua.yandex.jere184.c4tappydefender.di.SAVED_TEXT
import ua.yandex.jere184.c4tappydefender.logging.logD
import ua.yandex.jere184.c4tappydefender.model.User
import ua.yandex.jere184.c4tappydefender.ui.GameActivity
import ua.yandex.jere184.c4tappydefender.util.NICK_NAME_KEY
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
    private val userRecordsObserver = Observer<List<UserRecordEntity>> { userRecords ->
        val strBuilder = StringBuilder("time | dist | time (sec) \n")
        userRecords.forEach {
            strBuilder.append("${it.currentTime} | ${it.dist} | ${it.time/1000}")
            strBuilder.append("\n")

        }
        setMyRecord(strBuilder.toString())
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
        init()
        pressedLeftBtn()
        pressedRightBtn()
        pressedShipIcon()
        pressedStartBtn()
    }

    private fun subscribeUserRecords() {
        viewModel.userRecordsFlow.observe(viewLifecycleOwner, userRecordsObserver)
    }

    private fun init() {
        viewModel.getUserDataLiveData().observe(viewLifecycleOwner, userDataObserver)
        viewModel.readUserData(SAVED_TEXT)
        setShipIcon(getShipBitmap(R.drawable.spaceship_1))
        subscribeUserRecords()
    }

    private fun pressedStartBtn() {
        binding.btnStart.setOnClickListener {
            Public.playerShipType = viewModel.currentPayerShipIndex.toByte()
            val intent = Intent(requireContext(), GameActivity::class.java)
            intent.putExtra(EXTRA_COUNT_FOR_SHIP, viewModel.currentPayerShipIndex)
            startActivity(intent)
        }
    }

    private fun pressedShipIcon() {
        binding.ivShip.setOnClickListener {
            setShipIcon(getShipBitmap(viewModel.nextShipIcon()))
        }
    }

    private fun pressedRightBtn() {
        binding.btnRight.setOnClickListener {
            setShipIcon(getShipBitmap(viewModel.nextShipIcon()))
        }
    }

    private fun pressedLeftBtn() {
        binding.btnLeft.setOnClickListener {
            setShipIcon(getShipBitmap(viewModel.nextShipIcon()))
        }
    }

    private fun setMyRecord(record: String) {
        binding.tvMyFirstPlace.text = record
    }

    private fun getShipBitmap(iconId: Int): Bitmap {
        return Public.scaleBitmap(
            BitmapFactory.decodeResource(requireContext().resources, iconId),
            3.toByte()
        )
    }


    private fun setUserNickName(name: String) {
        binding.etNickName.text = name.toEditable()
    }

    private fun setShipIcon(icon: Bitmap) {
        binding.ivShip.setImageBitmap(icon)
    }

    override fun onPause() {
        saveNickname()
        super.onPause()
    }

    private fun saveNickname() {
        viewModel.saveUserData(NICK_NAME_KEY, User(getNickName()))
    }

    private fun getNickName(): String {
        return binding.etNickName.text.toString()
    }

    private fun log(msg: String = "") {
        logD(msg)
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val EXTRA_COUNT_FOR_SHIP = "c4tappydefender.t_payerShipIndex"
    }

}