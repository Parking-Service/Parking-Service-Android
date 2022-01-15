package com.app.service.parking.adapter.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.service.parking.databinding.ItemReviewBinding
import com.app.service.parking.presentation.base.BaseDiffUtil
import com.app.service.parking.listener.RecyclerItemClickListener
import com.app.service.parking.model.dto.Review
import com.bumptech.glide.Glide
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MoreReviewRVAdapter(val listener: RecyclerItemClickListener) :
    RecyclerView.Adapter<MoreReviewRVAdapter.ViewHolder>() {

    val reviewList = ArrayList<Review>()

    init {
        setHasStableIds(true)
    }

    inner class ViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val reviewItem = reviewList[position]
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")

            with(binding) {
                model = reviewItem // 바인딩 모델 지정
                root.setOnClickListener { listener.onClick(position) } // 주소 리스트 아이템을 클릭했을 때 작동한다.
                reviewRatingBar.rating = reviewItem.reviewRate!! // 리뷰 래이팅 설정
                reviewDateTextView.text =
                    dateFormat.format(Date(reviewItem.reviewDate!!)) // 리뷰 등록 일시

                if(!reviewItem.reviewImageUrl.isNullOrBlank()) { // 리뷰 이미지가 존재한다면
                    reviewImageHolder.visibility = View.VISIBLE // 리뷰 이미지뷰를 보여준다.
                    Glide.with(binding.root).load(reviewItem.reviewImageUrl.replace("https","http"))
                        .into(reviewImageView) // 주차장 리뷰 이미지 불러오기
                }
            }
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

    fun updateItems(newReviewList: List<Review>) {
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
