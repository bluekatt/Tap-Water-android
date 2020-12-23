package com.android.example.tapwater.ui.summary

import android.view.LayoutInflater
import android.view.View
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
            if(position==totalCount-1) binding.rightButton.visibility = View.GONE
            if(position==0) binding.leftButton.visibility = View.GONE
            binding.executePendingBindings()
        }
    }

    private val itemList: MutableList<MonthSummaryItem> = mutableListOf()

    fun setItemList(recordList: List<DayRecord>) {
        recordList.forEach { record ->
            val item = itemList.find { it.month == record.date.substring(0, 6) }
            if(item == null) {
                itemList.add(MonthSummaryItem(record))
            } else {
                if(record.drankToday!=0f) item.totalGoal += record.dailyGoal
                item.totalDrank += record.drankToday
                item.achievedDays += if(record.drankToday >= record.dailyGoal) 1 else 0
                if(item.mostDrank <= record.drankToday) {
                    item.mostDrankDate = record.date
                    item.mostDrank = record.drankToday
                }
            }
        }

        itemList.sortBy { it.month }

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

data class MonthSummaryItem(private val firstRecord: DayRecord) {
    val month = firstRecord.date.substring(0, 6)
    var totalGoal = firstRecord.dailyGoal
    var totalDrank = firstRecord.drankToday
    var achievedDays = if(firstRecord.drankToday >= firstRecord.dailyGoal) 1 else 0
    var mostDrankDate = firstRecord.date
    var mostDrank = firstRecord.drankToday
}