package com.android.example.tapwater.ui.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DayRecord
import com.android.example.tapwater.databinding.ItemMonthViewBinding

class MonthSummaryAdapter(val monthSummaryListener: MonthSummaryListener): RecyclerView.Adapter<MonthSummaryAdapter.MonthViewHolder>() {
    class MonthViewHolder(val binding: ItemMonthViewBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MonthViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ItemMonthViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_month_view, parent, false)

                return MonthViewHolder(binding)
            }
        }

        fun bind(item: MonthSummaryItem, position: Int, totalCount: Int, monthSummaryListener: MonthSummaryListener) {
            binding.item = item
            binding.position = position
            binding.clickListener = monthSummaryListener
            binding.showLeft = position!=0
            binding.showRight = position!=totalCount-1
            binding.executePendingBindings()
        }
    }

    private var itemList: List<MonthSummaryItem> = emptyList()

    fun setItemList(list: List<MonthSummaryItem>) {
        itemList = list

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, position, itemCount, monthSummaryListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}

class MonthSummaryListener(val clickListener: (position: Int) -> Unit) {
    fun onClick(position: Int) = clickListener(position)
}

data class MonthSummaryItem(val month: String) {
    var totalGoal = 0f
    var totalDrank = 0f
    var achievedDays = 0
    var mostDrankDate: String? = null
    var mostDrank = 0f

    constructor(firstRecord: DayRecord): this(firstRecord.date.substring(0,6)) {
        totalGoal = firstRecord.dailyGoal
        totalDrank = firstRecord.drankToday
        achievedDays = if(firstRecord.drankToday >= firstRecord.dailyGoal) 1 else 0
        mostDrankDate = firstRecord.date
        mostDrank = firstRecord.drankToday
    }
}