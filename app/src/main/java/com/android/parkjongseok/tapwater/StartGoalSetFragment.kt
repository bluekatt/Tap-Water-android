package com.android.parkjongseok.tapwater

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.parkjongseok.tapwater.databinding.FragmentStartGoalSetBinding
import javax.inject.Inject

class StartGoalSetFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: StartGoalSetViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding: FragmentStartGoalSetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_goal_set, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val stepSize = 0.1f
        val minValue = (0 / stepSize).toInt()
        val maxValue = (5 / stepSize).toInt()
        binding.numberPicker.minValue = minValue
        binding.numberPicker.maxValue = maxValue
        binding.numberPicker.displayedValues = Array(((maxValue - minValue+1)/stepSize).toInt()) { i ->
            val value = (i + minValue) * stepSize
            requireContext().getString(R.string.liter_format_short, value)
        }
        binding.numberPicker.wrapSelectorWheel = false

        binding.goalSetCompletedButton.setOnClickListener {
            viewModel.onCompletedClick(binding.numberPicker.value * stepSize)
        }

        viewModel.navigateToSpeedMeasure.observe(viewLifecycleOwner, {
            findNavController().navigate(StartGoalSetFragmentDirections.actionStartGoalSetFragmentToSpeedMeasureFragment())
        })

        return binding.root
    }

}