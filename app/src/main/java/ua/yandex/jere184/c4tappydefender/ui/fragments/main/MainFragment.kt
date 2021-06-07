package ua.yandex.jere184.c4tappydefender.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.yandex.jere184.c4tappydefender.databinding.MainFragmentBinding
import javax.inject.Inject

class MainFragment : Fragment() {


    private var bindingImpl: MainFragmentBinding? = null
    private val binding get() = bindingImpl!!

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingImpl = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}