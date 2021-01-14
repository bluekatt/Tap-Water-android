package com.parkjongseok.tapwater.ui.settings

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.parkjongseok.tapwater.MyApplication
import com.parkjongseok.tapwater.databinding.FragmentSpeedMeasureCupsBinding
import javax.inject.Inject

class SpeedMeasureCupsFragment : Fragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SpeedMeasureViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding = FragmentSpeedMeasureCupsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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
}