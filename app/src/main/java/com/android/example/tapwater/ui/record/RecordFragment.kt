package com.android.example.tapwater.ui.record

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentRecordBinding
import javax.inject.Inject

class RecordFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: RecordViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        val binding: FragmentRecordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

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

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        viewModel.initializeDayRecord()
    }
}