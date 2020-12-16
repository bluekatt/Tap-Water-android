package com.android.example.tapwater.ui.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentSpeedMeasureBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SpeedMeasureFragment(val preference: SpeedPreference) : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(preference: SpeedPreference): SpeedMeasureFragment = SpeedMeasureFragment(preference)
    }

    @Inject lateinit var viewModel: SpeedMeasureViewModel
    private lateinit var binding: FragmentSpeedMeasureBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speed_measure, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.measureComplete.observe(viewLifecycleOwner, {
            if(it && viewModel.speed.value!=null) {
                preference.doPersistFloat(viewModel.speed.value!!)
                dismiss()
            }
        })


        binding.cupButton1.setOnTouchListener { v, event ->
            if(v.isClickable) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> (v as ImageView).colorFilter = BlendModeColorFilter(Color.rgb(209, 209, 209), BlendMode.SRC_IN)
                    MotionEvent.ACTION_UP -> {
                        (v as ImageView).clearColorFilter()
                        v.performClick()
                    }
                    MotionEvent.ACTION_CANCEL -> (v as ImageView).clearColorFilter()
                }
            }
            true
        }

        binding.cupButton2.setOnTouchListener { v, event ->
            if(v.isClickable) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> (v as ImageView).colorFilter = BlendModeColorFilter(Color.rgb(209, 209, 209), BlendMode.SRC_IN)
                    MotionEvent.ACTION_UP -> {
                        (v as ImageView).clearColorFilter()
                        v.performClick()
                    }
                    MotionEvent.ACTION_CANCEL -> (v as ImageView).clearColorFilter()
                }
            }
            true
        }

        binding.cupButton3.setOnTouchListener { v, event ->
            if(v.isClickable) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> (v as ImageView).colorFilter = BlendModeColorFilter(Color.rgb(209, 209, 209), BlendMode.SRC_IN)
                    MotionEvent.ACTION_UP -> {
                        (v as ImageView).clearColorFilter()
                        v.performClick()
                    }
                    MotionEvent.ACTION_CANCEL -> (v as ImageView).clearColorFilter()
                }
            }
            true
        }

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