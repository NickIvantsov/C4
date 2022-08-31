package com.example.feature_game

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.core.interactor.SpaceDustUseCase
import com.example.feature_game.databinding.GameFragmentBinding
import com.example.feature_game.model.GameViewParams
import com.example.feature_game.repository.IMeteoriteRepository
import com.example.feature_game.tmp.SpaceView
import com.example.feature_game.tmp.SpaceViewModel
import com.example.repository.IUserRecordRepository
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class GameFragment : Fragment() {


    private var gameView: SpaceView? = null

    @Inject
    lateinit var userRecordRepository: IUserRecordRepository

    @Inject
    lateinit var viewModel: GameViewModel

    @Inject
    lateinit var meteoriteRepository: IMeteoriteRepository

    @Inject
    lateinit var spaceDustUseCase: SpaceDustUseCase

    @Inject
    lateinit var spaceViewModel: SpaceViewModel


    private var bindingImpl: GameFragmentBinding? = null
    private val binding get() = bindingImpl!!

    private val random = Random()
    val point = Point()
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
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

        val args: GameFragmentArgs by navArgs()
        gameView = SpaceView(
            requireContext(),
            userRecordRepository,
            random,
            point,
            args.typeShip,
            meteoriteRepository,
            spaceDustUseCase,
            spaceViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingImpl = GameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        val args: GameFragmentArgs by navArgs()
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
        Log.d("MEASURE", "point: $point")
        binding.gameView.initialize(
            GameViewParams(
                userRecordRepository,
                random,
                point,
                args.typeShip,
                meteoriteRepository,
                spaceDustUseCase,
                spaceViewModel
            )
        )
        binding.swDebug.setOnCheckedChangeListener() { _, isChecked ->
            binding.gameView.debugEnable = isChecked
        }

        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                binding.tvFps.text = "FPS: ${seekBar?.progress}"
                binding.gameView.setFPSDivider(seekBar?.progress ?: 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                binding.tvFps.text = "FPS: ${seekBar?.progress}"
            }

        })
    }

    override fun onPause() {
        super.onPause()
        binding.gameView.pause()
    }

    override fun onResume() {
        com.example.core_utils.util.logging.hideSystemUI(requireActivity().window, binding.gameView)
        super.onResume()
        binding.gameView.resume()
    }


    companion object {
        private const val TAG = "GameFragment"
    }
}