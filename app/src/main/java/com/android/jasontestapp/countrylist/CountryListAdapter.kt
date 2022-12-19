package com.android.jasontestapp.countrylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.jasontestapp.R
import com.android.jasontestapp.data.CountryData

class CountryListAdapter: ListAdapter<CountryData, CountryListAdapter.CountryVH>(DiffCallback) {

    class CountryVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameRegion = itemView.findViewById<TextView>(R.id.name_region)
        private val capital = itemView.findViewById<TextView>(R.id.capital)
        private val countryCode = itemView.findViewById<TextView>(R.id.country_code)
        fun onBind(data: CountryData) {
            nameRegion.text = if (data.region.isNotBlank()) {
                "${data.name}, ${data.region}"
            } else {
                data.name
            }
            capital.apply {
                this.text = data.capital
                if (data.capital.isNotBlank()) {
                    this.visibility = View.VISIBLE
                } else {
                    this.visibility = View.GONE
                }
            }
            countryCode.text = data.code
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<CountryData>() {
        override fun areItemsTheSame(oldItem: CountryData, newItem: CountryData): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: CountryData, newItem: CountryData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_list_item, parent, false)
        return CountryVH(view)
    }

    override fun onBindViewHolder(holder: CountryVH, position: Int) {
        holder.onBind(getItem(position))
    }
}