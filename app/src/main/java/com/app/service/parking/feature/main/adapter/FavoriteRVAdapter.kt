package com.app.service.parking.feature.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.service.parking.R
import com.app.service.parking.databinding.ItemSearchBinding
import com.app.service.parking.feature.base.BaseDiffUtil
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.model.dto.Lot
import timber.log.Timber
import java.lang.IndexOutOfBoundsException

class FavoriteRVAdapter(val listener: RecyclerItemClickListener) :
    RecyclerView.Adapter<FavoriteRVAdapter.ViewHolder>() {

    var lots = ArrayList<Lot>()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.model = lots[position] // 바인딩 모델 지정
            binding.root.setOnClickListener { listener.onClick(position) } // 주소 리스트 아이템을 클릭했을 때 작동
            binding.deleteButton.setOnClickListener {
                listener.onClick(
                    position,
                    R.id.delete_button
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return lots.size
    }

    override fun getItemId(position: Int): Long {
        return if (lots == null) {
            0
        } else lots[position].hashCode().toLong()
    }

    fun updateItems(newLots: ArrayList<Lot>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(lots, newLots), true)

        // Inconsistency detected 오류를 방지하기 위해 Temp리스트를 생성하여 기존 리스트를 먼저 건들지 않고 데이터 업데이트
        lots.clear()
        lots.addAll(newLots)

        diffResult.dispatchUpdatesTo(this) // 리사이클러뷰 업데이트
    }

    // IndexOutOfBoundsException 예외 처리를 위한 커스텀 LayoutManager
    class WrapContentLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                Timber.e("IndexOutOfBoundsException is occurred.")
            }
        }
    }

}

