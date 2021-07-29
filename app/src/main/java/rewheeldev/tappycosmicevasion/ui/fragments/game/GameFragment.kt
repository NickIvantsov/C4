package rewheeldev.tappycosmicevasion.ui.fragments.game

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.android.support.AndroidSupportInjection
import rewheeldev.tappycosmicevasion.repository.IMeteoriteRepository
import com.example.repository.ISpaceDustRepository
import com.example.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.ui.customView.SpaceView
import rewheeldev.tappycosmicevasion.ui.customView.SpaceViewModel
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
    lateinit var spaceDustRepository: ISpaceDustRepository

    @Inject
    lateinit var spaceViewModel: SpaceViewModel

    private val random = Random()
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
            spaceDustRepository,
            spaceViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return gameView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    public override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    public override fun onResume() {
        com.example.core_utils.util.logging.hideSystemUI(requireActivity().window, gameView!!)
        super.onResume()
        gameView!!.resume()
    }


    companion object {
        private const val TAG = "GameFragment"
    }
}