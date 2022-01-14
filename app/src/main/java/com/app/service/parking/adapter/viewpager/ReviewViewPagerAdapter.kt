package com.app.service.parking.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.service.parking.presentation.review.more.*

class ReviewViewPagerAdapter(activity: MoreReviewActivity): FragmentStateAdapter(activity) {

    // ViewPager2에 연결할 Fragemnt 생성
    val fragmentList = listOf<Fragment>(AllReviewFragment(), GoodReviewFragment(), NormalReviewFragment(), BadReviewFragment())

    // ViesPager2에서 노출시킬 Fragment 개수 설정
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    // ViewPager2의 각 페이지에서 노출할 Fragment 설정
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }


}