package com.android.example.tapwater.ui.settings

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentSpeedMeasureHelpBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SpeedMeasureHelpFragment : BottomSheetDialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SpeedMeasureViewModel by viewModels({ targetFragment!! }) { viewModelFactory }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding: FragmentSpeedMeasureHelpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_speed_measure_help, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.paperBagCupButton.setOnClickListener {
            val vol = binding.paperBagCupVolume.text.toString()
            viewModel.onCupTypeClicked(vol.substring(0, vol.length-2))
            dismiss()
        }

        binding.sojuGlassButton.setOnClickListener {
            val vol = binding.sojuGlassVolume.text.toString()
            viewModel.onCupTypeClicked(vol.substring(0, vol.length-2))
            dismiss()
        }

        binding.paperCupButton.setOnClickListener {
            val vol = binding.paperCupVolume.text.toString()
            viewModel.onCupTypeClicked(vol.substring(0, vol.length-2))
            dismiss()
        }

        binding.tallTeaCupButton.setOnClickListener {
            val vol = binding.tallTeaCupVolume.text.toString()
            viewModel.onCupTypeClicked(vol.substring(0, vol.length-2))
            dismiss()
        }

        binding.beerGlassButton.setOnClickListener {
            val vol = binding.beerGlassVolume.text.toString()
            viewModel.onCupTypeClicked(vol.substring(0, vol.length-2))
            dismiss()
        }

        return binding.root
    }

}