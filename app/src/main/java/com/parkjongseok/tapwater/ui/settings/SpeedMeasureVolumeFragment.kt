package com.parkjongseok.tapwater.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.parkjongseok.tapwater.MyApplication
import com.parkjongseok.tapwater.databinding.FragmentSpeedMeasureVolumeBinding
import javax.inject.Inject

class SpeedMeasureVolumeFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SpeedMeasureViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding = FragmentSpeedMeasureVolumeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.manualInput.observe(viewLifecycleOwner, {
            if(it) {
                binding.volumeInput.requestFocus()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        })

        return binding.root
    }
}