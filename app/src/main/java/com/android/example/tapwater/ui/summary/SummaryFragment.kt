package com.android.example.tapwater.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.databinding.FragmentSummaryBinding
import com.android.example.tapwater.dateStringToComponents
import com.android.example.tapwater.ui.record.RecordDetailFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import javax.inject.Inject

class SummaryFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SummaryViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        val binding: FragmentSummaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.calendarView.state().edit()
            .setMaximumDate(viewModel.lastDate)
            .commit()

        binding.calendarView.topbarVisible = false

        binding.calendarView.setOnMonthChangedListener { _, date ->
            viewModel.setMonthTitle(date.year, date.month, CalendarDay.today().year != date.year)
        }

        val selectedDayDecorator = SelectedDayDecorator(viewModel.selectedDate.value, 0f)

        binding.calendarView.addDecorators(
            selectedDayDecorator,
            SelectionDecorator(requireActivity())
        )

        binding.calendarView.selectedDate = viewModel.selectedDate.value

        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            selectedDayDecorator.date = date
            viewModel.setSelectedRecord(date)
        }

        viewModel.firstDate.observe(viewLifecycleOwner, {
            binding.calendarView.state().edit()
                .setMinimumDate(it)
                .commit()
        })

        viewModel.records.observe(viewLifecycleOwner, {
            it.forEach { record ->
                val components = dateStringToComponents(record.date)
                val calendarDay = CalendarDay.from(components[0], components[1], components[2])
                binding.calendarView.addDecorator(RecordDecorator(calendarDay, record.drankToday/record.dailyGoal))
            }
        })

        viewModel.selectedRecord.observe(viewLifecycleOwner, {
            if(it != null) {
                selectedDayDecorator.percentage = it.drankToday / it.dailyGoal
            } else {
                selectedDayDecorator.percentage = 0f
            }
            binding.calendarView.invalidateDecorators()
        })

        viewModel.navigateToMonthSummary.observe(viewLifecycleOwner, {
            if(it) {
                val monthSummaryFragment = StatsFragment()
                monthSummaryFragment.show(parentFragmentManager, "MonthSummary")
                viewModel.onStatsNavigated()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            if(it!=null) {
                val recordDetailFragment = RecordDetailFragment()
                recordDetailFragment.arguments = bundleOf("recordDate" to it)
                recordDetailFragment.show(parentFragmentManager, "recordDetail")
                viewModel.onDetailNavigated()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSelectedRecord(viewModel.selectedDate.value!!)
    }
}