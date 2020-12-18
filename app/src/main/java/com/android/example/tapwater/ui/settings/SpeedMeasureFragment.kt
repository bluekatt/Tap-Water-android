package com.android.example.tapwater.ui.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.android.example.tapwater.MainActivity
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.StartActivity
import com.android.example.tapwater.databinding.FragmentSpeedMeasureBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SpeedMeasureFragment(private val speedPreference: SpeedPreference? = null): BottomSheetDialogFragment() {
    companion object {
        fun newInstance(speedPreference: SpeedPreference): SpeedMeasureFragment {
            return SpeedMeasureFragment(speedPreference)
        }
    }

    @Inject lateinit var viewModel: SpeedMeasureViewModel
    private lateinit var binding: FragmentSpeedMeasureBinding
    private lateinit var pref: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_speed_measure, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.measureComplete.observe(viewLifecycleOwner, {
            if(it && viewModel.speed.value!=null) {
                pref.edit().putFloat("speed", viewModel.speed.value!!).apply()
                speedPreference?.updateSummary()
                if(pref.getBoolean("first_launch", true)) {
                    pref.edit().putBoolean("first_launch", false).apply()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                dismiss()
            }
        })

        val touchListener = {v: View, event: MotionEvent ->
            if(v.isClickable) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            (v as ImageView).colorFilter = BlendModeColorFilter(Color.rgb(209, 209, 209), BlendMode.SRC_IN)
                        } else {
                            (v as ImageView).setColorFilter(Color.rgb(209, 209, 209), PorterDuff.Mode.SRC_IN)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        (v as ImageView).clearColorFilter()
                        v.performClick()
                    }
                    MotionEvent.ACTION_CANCEL -> (v as ImageView).clearColorFilter()
                }
            }
            true
        }

        binding.cupButton1.setOnTouchListener(touchListener)
        binding.cupButton2.setOnTouchListener(touchListener)
        binding.cupButton3.setOnTouchListener(touchListener)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.root.layoutParams.height = (requireActivity().resources.displayMetrics.heightPixels)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return (dialog as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }
}