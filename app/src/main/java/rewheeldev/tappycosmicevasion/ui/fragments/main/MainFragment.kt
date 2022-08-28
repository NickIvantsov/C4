package rewheeldev.tappycosmicevasion.ui.fragments.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core_utils.util.logging.toEditable
import com.example.core_utils.util.scaleBitmap
import com.example.model.User
import dagger.android.support.AndroidSupportInjection
import rewheeldev.tappycosmicevasion.R
import rewheeldev.tappycosmicevasion.databinding.MainFragmentBinding
import rewheeldev.tappycosmicevasion.di.modules.SAVED_TEXT
import rewheeldev.tappycosmicevasion.logging.logD
import rewheeldev.tappycosmicevasion.ui.adapters.UserRecordsAdapter
import javax.inject.Inject

class MainFragment : Fragment() {

    private var bindingImpl: MainFragmentBinding? = null
    private val binding get() = bindingImpl!!
    private val point = Point()

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var userRecordsAdapter: UserRecordsAdapter

    private val userDataObserver = Observer<User> { userData ->
        userData.nickName?.let {
            val name = if (it.isEmpty()) "name" else it
            setUserNickName(name)
        }
    }
    private val userRecordsObserver =
        Observer<List<com.example.model.UserRecordEntity>> { userRecords ->
            userRecordsAdapter.addRecords(userRecords)
        }
    var mainView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        val point = Point()
        val display =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                requireActivity().display
            } else {
                requireActivity().windowManager.defaultDisplay
            }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = requireActivity().windowManager.currentWindowMetrics
            point.x = windowMetrics.bounds.width()
            point.y = windowMetrics.bounds.height()
        } else display?.getSize(
            point
        )
//        mainView = MainView(requireContext(),point)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingImpl = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
//        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        pressedLeftBtn()
        pressedRightBtn()
        pressedShipIcon()
        pressedStartBtn()
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    private fun subscribeUserRecords() {
        viewModel.userRecordsFlow.observe(viewLifecycleOwner, userRecordsObserver)
    }

    private fun init() {
        viewModel.getUserDataLiveData().observe(viewLifecycleOwner, userDataObserver)
        viewModel.readUserData(SAVED_TEXT)
        setShipIcon(getShipBitmap(R.drawable.spaceship_1))
        subscribeUserRecords()
        binding.rvUserRecords.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserRecords.adapter = userRecordsAdapter
    }

    private fun pressedStartBtn() {
        binding.btnStart.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToGameFragment(viewModel.currentPayerShipIndex)
            findNavController().navigate(action)
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

    private fun getShipBitmap(iconId: Int): Bitmap {

        val display =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                requireActivity().display
            } else {
                requireActivity().windowManager.defaultDisplay
            }

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            scaleBitmap(
                BitmapFactory.decodeResource(requireContext().resources, iconId),
                3,
                windowMetrics.bounds.width()
            )
        } else {
            display?.getSize(point)
            scaleBitmap(
                BitmapFactory.decodeResource(requireContext().resources, iconId),
                3,
                point.x
            )
        }
    }


    private fun setUserNickName(name: String) {
        binding.etNickName.text = name.toEditable()
    }

    private fun setShipIcon(icon: Bitmap) {
        binding.ivShip.setImageBitmap(icon)
    }

    override fun onPause() {
//        saveNickname()
        mainView?.pause()
        super.onPause()
    }

    override fun onResume() {
        mainView?.resume()
        super.onResume()
    }

    private fun saveNickname() {
        viewModel.saveUserData(
            com.example.core_utils.util.logging.NICK_NAME_KEY,
            User(getNickName())
        )
    }

    private fun getNickName(): String {
        return binding.etNickName.text.toString()
    }

    private fun log(msg: String = "") {
        logD(msg)
    }

    companion object {
        fun newInstance() = MainFragment()
        const val EXTRA_COUNT_FOR_SHIP = "c4tappydefender.t_payerShipIndex"
    }

}