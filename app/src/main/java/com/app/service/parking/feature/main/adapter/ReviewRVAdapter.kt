package com.app.service.parking.feature.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.service.parking.R
import com.app.service.parking.databinding.ItemReviewBinding
import com.app.service.parking.feature.base.BaseDiffUtil
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import timber.log.Timber

class ReviewRVAdapter(val listener: RecyclerItemClickListener) :
    RecyclerView.Adapter<ReviewRVAdapter.ViewHolder>() {

    val reviewList = ArrayList<Review>()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.model = reviewList[position] // 바인딩 모델 지정
            binding.root.setOnClickListener { listener.onClick(position) } // 주소 리스트 아이템을 클릭했을 때 작동
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun getItemId(position: Int): Long {
        return if (reviewList == null) {
            0
        } else reviewList[position].hashCode().toLong()
    }

    fun updateItems(newReviewList: ArrayList<Review>) {
        val diffResult = DiffUtil.calculateDiff(BaseDiffUtil(reviewList, newReviewList), true)

        // Inconsistency detected 오류를 방지하기 위해 Temp리스트를 생성하여 기존 리스트를 먼저 건들지 않고 데이터 업데이트
        reviewList.clear()
        reviewList.addAll(newReviewList)

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