package com.android.example.tapwater.ui.record

import android.animation.AnimatorInflater
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.addListener
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.example.tapwater.MyApplication
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DrinkLogItem
import com.android.example.tapwater.databinding.FragmentRecordDetailBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RecordDetailFragment : BottomSheetDialogFragment(), OnChartValueSelectedListener {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: RecordDetailViewModel by viewModels { viewModelFactory }

    private lateinit var chart: LineChart
    private val df = SimpleDateFormat("HH", Locale.getDefault())
    private var lastTime = df.format(Date()).toInt()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        (requireActivity().application as MyApplication).appComponent.inject(this)

        val recordDate = requireArguments().getString("recordDate")
        val dateDF = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val today = dateDF.format(Date())
        if(today != recordDate) lastTime = 23
        viewModel.lastTime = lastTime
        viewModel.setRecord(recordDate)

        val binding: FragmentRecordDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_detail, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        chart = binding.recordChart

        chart.setNoDataText(requireContext().getString(R.string.detail_chart_no_data_text))
        chart.setNoDataTextColor(requireContext().getColor(R.color.primaryColor))
        chart.marker = HighlightMarkerView(requireContext(), null, R.layout.highlight_marker)
        chart.setMaxVisibleValueCount(0)
        chart.setScaleEnabled(false)
        chart.extraBottomOffset = 10f
        chart.setOnChartValueSelectedListener(this)

        val xAxisFormatter = DetailChartXAxisFormatter(lastTime+2, requireContext().resources)
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            yOffset = 10f
            valueFormatter = xAxisFormatter
            setDrawGridLines(false)
            setDrawAxisLine(false)
        }

        val yAxisFormatter = DetailChartYAxisFormatter(requireContext().resources)
        chart.axisRight.apply {
            axisMinimum = 0f
            granularity = 0.2f
            xOffset = 15f
            isGranularityEnabled = true
            valueFormatter = yAxisFormatter
            setDrawAxisLine(false)
        }

        chart.axisLeft.isEnabled = false
        chart.legend.isEnabled = false
        chart.description.isEnabled = false

        val adapter = DrinkLogAdapter()
        binding.logList.adapter = adapter
        binding.logList.layoutManager = LinearLayoutManager(activity)

        val scale = resources.displayMetrics.density * 20000
        binding.chartLayout.cameraDistance = scale
        binding.logLayout.cameraDistance = scale

        binding.logLayout.visibility = View.GONE
        binding.chartButton.visibility = View.GONE

        binding.logList.isNestedScrollingEnabled = false

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.removeRecordButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle(getString(R.string.detail_dialog_remove_record_title))
            builder.setMessage(getString(R.string.detail_dialog_remove_record_content))

            builder.setNegativeButton(getString(R.string.detail_dialog_cancel_button)) { dialog, _ ->
                dialog.cancel()
            }

            builder.setPositiveButton(getString(R.string.detail_dialog_remove_button)) { _, _ ->
                viewModel.removeRecord()
                Toast.makeText(requireContext(), getString(R.string.detail_remove_toast), Toast.LENGTH_LONG).show()
            }

            builder.show()
        }

        val leftIn = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_left_in)
        val leftOut = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_left_out)
        val rightOut = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_right_out)
        val rightIn = AnimatorInflater.loadAnimator(requireContext(), R.animator.card_flip_right_in)

        viewModel.showChart.observe(viewLifecycleOwner, {
            if(viewModel.firstShow) {
                viewModel.firstShow = false
                return@observe
            }

            if(it) {
                leftIn.setTarget(binding.chartLayout)
                leftOut.setTarget(binding.logLayout)

                binding.chartLayout.visibility = View.VISIBLE
                leftIn.start()
                leftOut.start()
                leftOut.addListener(onEnd = {
                    binding.logLayout.visibility = View.GONE
                    binding.chartButton.visibility = View.GONE
                    binding.listButton.visibility = View.VISIBLE
                    binding.listButton.isEnabled = true
                    binding.logList.isNestedScrollingEnabled = false
                }, onStart = {
                    binding.chartLayout.visibility = View.VISIBLE
                    binding.chartButton.isEnabled = false
                })
            } else {
                rightIn.setTarget(binding.logLayout)
                rightOut.setTarget(binding.chartLayout)

                binding.logLayout.visibility = View.VISIBLE
                rightIn.start()
                rightOut.start()
                rightOut.addListener(onEnd = {
                    binding.chartLayout.visibility = View.GONE
                    binding.listButton.visibility = View.GONE
                    binding.chartButton.visibility = View.VISIBLE
                    binding.chartButton.isEnabled = true
                    binding.logList.isNestedScrollingEnabled = true
                }, onStart = {
                    binding.logLayout.visibility = View.VISIBLE
                    binding.listButton.isEnabled = false
                })
            }
        })

        viewModel.record.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    setChartData(it.drinkLog)
                    val limitLine = LimitLine(it.dailyGoal, requireContext().getString(R.string.record_detail_goal))
                    limitLine.lineColor = requireContext().getColor(R.color.primaryDarkColor)
                    chart.axisRight.apply {
                        addLimitLine(limitLine)
                        if(axisMaximum<it.dailyGoal) axisMaximum = it.dailyGoal * 1.2f
                    }

                    adapter.submitList(it.drinkLog)
                    adapter.notifyDataSetChanged()
                }
            }
        })

        if(today!=recordDate) {
            binding.removeRecordButton.visibility = View.GONE
        }

        return binding.root
    }

    private fun setChartData(log: List<DrinkLogItem>) {
        val values = MutableList(lastTime+2) { index ->
            Entry(index.toFloat(), 0f)
        }
        log.forEach {
            val hour = it.time.substring(0,2).toInt()
            values[hour+1].y += it.volume
        }
        val valuesIterator = values.listIterator()
        while(valuesIterator.hasNext()) {
            val cur = valuesIterator.next()
            if(!valuesIterator.hasNext()) break
            val next = valuesIterator.next()
            next.y += cur.y
            valuesIterator.previous()
        }

        val dataSet = LineDataSet(values, "record")
        val color = requireContext().getColor(R.color.primaryColor)
        dataSet.color = color
        dataSet.axisDependency = YAxis.AxisDependency.RIGHT
        dataSet.setDrawCircles(true)
        dataSet.circleRadius = 6f
        dataSet.circleHoleRadius = 2f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = requireContext().getColor(R.color.primaryTransparentColor)
        dataSet.setDrawHighlightIndicators(false)

        val data = LineData(dataSet)

        chart.data = data
        chart.invalidate()
        chart.highlightValue((lastTime+1).toFloat(), 0)
        chart.zoom(values.size/6f, 1f, lastTime.toFloat(), 0f, YAxis.AxisDependency.RIGHT)
        chart.animateY(500, Easing.EaseInOutCubic)
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
        viewModel.setSelectedTimeAndVolume(e.x, e.y)
    }

    override fun onNothingSelected() {
        chart.highlightValue(viewModel.selectedTime.value!!, 0)
    }

    override fun onDestroy() {
        if(targetFragment is RecordFragment)
            (targetFragment as RecordFragment).viewModel.initializeDayRecord()
        super.onDestroy()
    }

}

class DetailChartXAxisFormatter(val size: Int, val res: Resources): ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if(value.toInt()==size-1 && value!=25f) res.getString(R.string.record_detail_now) else res.getString(R.string.time_format_short, value.toInt())
    }
}

class DetailChartYAxisFormatter(val res: Resources): ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return res.getString(R.string.liter_format_short, value)
    }
}

class HighlightMarkerView(context: Context, attrs: AttributeSet? = null, layoutResource: Int): MarkerView(context, layoutResource) {
    override fun draw(canvas: Canvas) {
        canvas.translate(-(width/2).toFloat(), -(height/2).toFloat())
        super.draw(canvas)
    }
}