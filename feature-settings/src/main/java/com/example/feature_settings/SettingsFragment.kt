package com.example.feature_settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.feature_settings.databinding.SettingsFragmentBinding
import javax.inject.Inject

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    @Inject
    lateinit var viewModel: SettingsViewModel
    private var bindingImpl: SettingsFragmentBinding? = null
    private val binding get() = bindingImpl!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingImpl = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}