package com.android.parkjongseok.tapwater.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.parkjongseok.tapwater.databinding.ListLicenseTextViewBinding

class OpenSourceLicensesAdapter(val listener: LicenseListener): ListAdapter<OpenSourceLicensesInfo, OpenSourceLicensesAdapter.TextItemViewHolder>(OpenSourceLicensesCallback()) {
    class TextItemViewHolder(val binding: ListLicenseTextViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListLicenseTextViewBinding.inflate(layoutInflater, parent, false)

        return TextItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.item = item
        holder.binding.clickListener = listener
        holder.binding.executePendingBindings()
    }
}

class LicenseListener(val clickListener: (title: String, content: String) -> Unit) {
    fun onClick(item: OpenSourceLicensesInfo) = clickListener(item.licenseTitle, item.licenseContent)
}

class OpenSourceLicensesCallback: DiffUtil.ItemCallback<OpenSourceLicensesInfo>() {
    override fun areItemsTheSame(
        oldItem: OpenSourceLicensesInfo,
        newItem: OpenSourceLicensesInfo,
    ): Boolean {
        return oldItem.licenseTitle == newItem.licenseContent
    }

    override fun areContentsTheSame(
        oldItem: OpenSourceLicensesInfo,
        newItem: OpenSourceLicensesInfo,
    ): Boolean {
        return oldItem == newItem
    }

}