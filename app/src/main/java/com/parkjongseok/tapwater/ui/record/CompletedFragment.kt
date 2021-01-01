package com.parkjongseok.tapwater.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.parkjongseok.tapwater.MainActivity
import com.parkjongseok.tapwater.R
import com.parkjongseok.tapwater.databinding.FragmentCompletedBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CompletedFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentCompletedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)

        binding.confirmButton.setOnClickListener {
            dismiss()
        }

        binding.showCalendarButton.setOnClickListener {
            (requireActivity() as MainActivity).navView.selectedItemId = R.id.navigation_summary
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
}