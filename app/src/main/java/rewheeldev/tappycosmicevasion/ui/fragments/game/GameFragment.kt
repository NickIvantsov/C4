package rewheeldev.tappycosmicevasion.ui.fragments.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.android.support.AndroidSupportInjection
import rewheeldev.tappycosmicevasion.repository.IUserRecordRepository
import rewheeldev.tappycosmicevasion.ui.customView.TDView
import rewheeldev.tappycosmicevasion.util.hideSystemUI
import javax.inject.Inject

class GameFragment : Fragment() {


    private var gameView: TDView? = null

    @Inject
    lateinit var userRecordRepository: IUserRecordRepository

    @Inject
    lateinit var viewModel: GameViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        gameView = TDView(requireContext(), userRecordRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return gameView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: GameFragmentArgs by navArgs()
        Log.d(TAG, "shipType = ${args.typeShip2}")
        tmpCount = 1//args.typeShip2
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "Fragment back pressed invoked")
                findNavController().popBackStack()
                /*if (isEnabled) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }*/
            }
        })
    }

    public override fun onPause() {
        super.onPause()
        gameView!!.pause()
    }

    public override fun onResume() {
        hideSystemUI(requireActivity().window, gameView!!)
        super.onResume()
        gameView!!.resume()
    }


    companion object {
        private const val TAG = "GameFragment"
        var tmpCount = 0

    }
}