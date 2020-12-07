package com.android.example.tapwater.ui.settings

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.databinding.DataBindingUtil
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.SpeedMeasureFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SpeedMeasureFragment(preference: SpeedPreference) : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(preference: SpeedPreference): SpeedMeasureFragment = SpeedMeasureFragment(preference)
    }

    @Inject lateinit var viewModel: SpeedMeasureViewModel
    private lateinit var binding: SpeedMeasureFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.speed_measure_fragment, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.root.layoutParams.height = (requireActivity().windowManager.currentWindowMetrics.bounds.height() * 0.9).toInt()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return (dialog as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

}