package com.app.service.parking.feature.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.service.parking.R
import com.app.service.parking.databinding.ItemSearchBinding
import com.app.service.parking.feature.base.BaseDiffUtil
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.model.dto.Lot

class SearchRVAdapter(val listener: RecyclerItemClickListener) : RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    var lots = ArrayList<Lot>()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.model = lots[position] // 바인딩 모델 지정
            binding.root.setOnClickListener { listener.onClick(position) } // 아이템 클릭 리스너
            binding.deleteButton.setOnClickListener { listener.onClick(position, R.id.delete_button) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return lots.size
    }

    override fun getItemId(position: Int): Long {
        return lots[position].hashCode().toLong()
    }

    fun updateItems(newLots: ArrayList<Lot>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(lots, newLots))

        lots.clear()
        lots.addAll(newLots)

        diffResult.dispatchUpdatesTo(this) // 리사이클러뷰 업데이트
    }

}