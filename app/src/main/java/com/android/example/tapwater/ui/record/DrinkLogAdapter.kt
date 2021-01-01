package com.android.example.tapwater.ui.record

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.tapwater.R
import com.android.example.tapwater.database.DrinkLogItem
import com.android.example.tapwater.databinding.ListItemDrinkLogBinding

class DrinkLogAdapter: ListAdapter<DrinkLogItem, DrinkLogAdapter.DrinkLogViewHolder>(DrinkLogDiffCallback()) {
    class DrinkLogViewHolder(val binding: ListItemDrinkLogBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): DrinkLogViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDrinkLogBinding.inflate(layoutInflater, parent, false)

                return DrinkLogViewHolder(binding)
            }
        }

        fun bind(item: DrinkLogItem) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkLogViewHolder {
        return DrinkLogViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DrinkLogViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class DrinkLogDiffCallback: DiffUtil.ItemCallback<DrinkLogItem>() {
    override fun areItemsTheSame(oldItem: DrinkLogItem, newItem: DrinkLogItem): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: DrinkLogItem, newItem: DrinkLogItem): Boolean {
        return oldItem == newItem
    }
}