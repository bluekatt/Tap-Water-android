package com.parkjongseok.tapwater.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.parkjongseok.tapwater.MainActivity
import com.parkjongseok.tapwater.MyApplication
import com.parkjongseok.tapwater.R
import com.parkjongseok.tapwater.databinding.FragmentSpeedMeasureBinding
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

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SpeedMeasureViewModel by viewModels({ requireActivity() }) { viewModelFactory }
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val binding: FragmentSpeedMeasureBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_speed_measure, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val navHostFragment = childFragmentManager.findFragmentById(R.id.speed_measure_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

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
                viewModel.willBeDismissed()
                dismiss()
            }
        })

        viewModel.navigateToHelpInfo.observe(viewLifecycleOwner, {
            if(it) {
                val speedMeasureHelpFragment = SpeedMeasureHelpFragment()
                speedMeasureHelpFragment.show(parentFragmentManager, "speedMeasureHelp")
                viewModel.onHelpInfoNavigated()
            }
        })

        viewModel.navigateToVolume.observe(viewLifecycleOwner, {
            if(it) {
                navController.navigate(SpeedMeasureGuideFragmentDirections.actionSpeedMeasureGuideFragmentToSpeedMeasureVolumeFragment())
                viewModel.onGuideNextNavigated()
            }
        })

        viewModel.navigateToCups.observe(viewLifecycleOwner, {
            if(it){
                navController.navigate(SpeedMeasureVolumeFragmentDirections.actionSpeedMeasureVolumeFragmentToSpeedMeasureCupsFragment())
                viewModel.onSetVolumeNavigated()
            }
        })

        viewModel.navigateToGuide.observe(viewLifecycleOwner, {
            if(it){
                navController.navigate(SpeedMeasureCupsFragmentDirections.actionSpeedMeasureCupsFragmentToSpeedMeasureGuideFragment())
                viewModel.onResetNavigated()
            }
        })

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        view?.layoutParams?.height = (requireActivity().resources.displayMetrics.heightPixels)
        dialog?.window?.setDimAmount(0f)
        (dialog as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeData()
    }
}