package com.parkjongseok.tapwater.ui.record

import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.parkjongseok.tapwater.MyApplication
import com.parkjongseok.tapwater.R
import com.parkjongseok.tapwater.databinding.FragmentRecordBinding
import javax.inject.Inject

class RecordFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: RecordViewModel by viewModels { viewModelFactory }

    private val dateChangeListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.initializeDayRecord()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        val binding: FragmentRecordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.showCompleted.observe(viewLifecycleOwner, {
            if(it) {
                val completedFragment = CompletedFragment()
                completedFragment.show(parentFragmentManager, "completed")
            }
        })

        viewModel.drankPercentage.observe(viewLifecycleOwner, {
            val origin = binding.waterBackground.percentage
            ValueAnimator.ofFloat(origin, it.toFloat()).apply {
                duration = if(origin==0F) 0 else 100
                addUpdateListener { animator ->
                    val percentage = animator.animatedValue as Float
                    binding.waterBackground.percentage = percentage
                }
                start()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            if(it) {
                val recordDetailFragment = RecordDetailFragment()
                val bundle = bundleOf("recordDate" to viewModel.todayRecord.date)
                recordDetailFragment.arguments = bundle
                recordDetailFragment.show(parentFragmentManager, "recordDetail")
                recordDetailFragment.setTargetFragment(this, 0)
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeDayRecord()
        requireActivity().registerReceiver(dateChangeListener, IntentFilter(Intent.ACTION_DATE_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(dateChangeListener)
    }
}