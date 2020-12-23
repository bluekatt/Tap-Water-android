package com.android.example.tapwater.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentStatsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class StatsFragment : BottomSheetDialogFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SummaryViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        val binding: FragmentStatsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = MonthSummaryAdapter(MonthSummaryListener { position ->
            binding.viewPager.currentItem = position
        })

        binding.viewPager.adapter = adapter

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        viewModel.records.observe(viewLifecycleOwner, {
            adapter.setItemList(it)
            binding.viewPager.setCurrentItem(adapter.itemCount-1, false)
        })

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