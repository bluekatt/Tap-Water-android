package com.android.parkjongseok.tapwater.ui.summary

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.android.parkjongseok.tapwater.MyApplication
import com.android.parkjongseok.tapwater.R
import com.android.parkjongseok.tapwater.databinding.FragmentStatsBinding
import com.android.parkjongseok.tapwater.getFormattedMonth
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class StatsFragment : BottomSheetDialogFragment(), OnChartValueSelectedListener {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: SummaryViewModel by viewModels({ requireActivity() }) { viewModelFactory }

    private lateinit var chart: BarChart
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stats, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = MonthSummaryAdapter(MonthSummaryListener { position ->
            binding.viewPager.currentItem = position
        })

        binding.viewPager.adapter = adapter

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        chart = binding.summaryChart
        chart.setNoDataText(requireContext().getString(R.string.summary_chart_no_data_text))
        chart.setNoDataTextColor(requireContext().getColor(R.color.primaryColor))
        chart.setOnChartValueSelectedListener(this)
        chart.extraBottomOffset = 20f
        chart.setMaxVisibleValueCount(0)
        chart.setScaleEnabled(false)
        chart.setFitBars(true)

        val xAxisFormatter = SummaryChartXAxisFormatter(resources)

        val xAxis = chart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawGridLines(false)
            setDrawAxisLine(false)
            valueFormatter = xAxisFormatter
        }

        val yAxisFormatter = SummaryChartYAxisFormatter(resources)

        val rightAxis = chart.axisRight
        rightAxis.apply {
            setDrawAxisLine(false)
            axisMinimum = 0f
            granularity = 0.2f
            isGranularityEnabled = true
            valueFormatter = yAxisFormatter
        }

        chart.axisLeft.isEnabled = false
        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        viewModel.monthItemList.observe(viewLifecycleOwner, { itemList ->
            xAxisFormatter.monthPosition = List(itemList.size) { index ->
                itemList[index].month
            }
            adapter.setItemList(itemList)
            binding.viewPager.setCurrentItem(itemList.lastIndex, false)
            setDataToChart(itemList)
        })

        return binding.root
    }

    private fun setDataToChart(itemList: List<MonthSummaryItem>){
        val values: MutableList<BarEntry> = mutableListOf()
        itemList.forEachIndexed { index, item ->
            values.add(BarEntry(index.toFloat(), item.totalDrank))
        }

        val dataSet = BarDataSet(values, "records")
        dataSet.color = requireContext().getColor(R.color.primaryColor)
        dataSet.highLightAlpha = 0
        dataSet.axisDependency = YAxis.AxisDependency.RIGHT

        val data = BarData(dataSet)
        data.barWidth = 0.5f

        chart.data = data
        chart.invalidate()
        chart.zoom(values.size/5f, 1f, values.lastIndex.toFloat(), 0f, YAxis.AxisDependency.RIGHT)
        chart.animateY(1000, Easing.EaseInOutCubic)
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

    override fun onValueSelected(e: Entry, h: Highlight?) {
        binding.viewPager.setCurrentItem(e.x.toInt(), false)
        binding.scrollView.smoothScrollTo(0, binding.monthSummaryTitle.bottom, 1000)
    }

    override fun onNothingSelected() {}

}

class SummaryChartXAxisFormatter(val res: Resources): ValueFormatter() {
    var monthPosition: List<String> = emptyList()

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val monthString = monthPosition.getOrNull(value.toInt())
        return getFormattedMonth(monthString, true ,res)
    }
}

class SummaryChartYAxisFormatter(val res: Resources): ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return res.getString(R.string.liter_format_short, value)
    }
}
