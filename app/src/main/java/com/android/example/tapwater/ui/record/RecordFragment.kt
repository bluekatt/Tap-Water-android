package com.android.example.tapwater.ui.record

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentRecordBinding
import javax.inject.Inject

class RecordFragment : Fragment() {

    @Inject lateinit var recordViewModel: RecordViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        val binding: FragmentRecordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        binding.viewModel = recordViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        recordViewModel.drankPercentage.observe(viewLifecycleOwner, Observer {
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

        return binding.root
    }
}