package com.parkjongseok.tapwater.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.parkjongseok.tapwater.MyApplication
import com.parkjongseok.tapwater.databinding.FragmentSpeedMeasureGuideBinding
import javax.inject.Inject

class SpeedMeasureGuideFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SpeedMeasureViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding = FragmentSpeedMeasureGuideBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}