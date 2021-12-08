package com.app.service.parking.feature.main.review

import android.os.Bundle
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityReviewBinding
import com.app.service.parking.feature.base.BaseActivity
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewActivity : BaseActivity<ActivityReviewBinding, ReviewViewModel>() {

    override val layoutResId: Int = R.layout.activity_review
    override val viewModel: ReviewViewModel by viewModel()
    lateinit var mapView: MapView // 카카오 맵 뷰

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initActivity() {
        mapView = MapView(this) // 카카오 맵 뷰 초기화
        binding.kakaoMapContainer.addView(mapView) // 지도란에 카카오 맵 뷰 추가
    }

    override fun finish() {
        binding.kakaoMapContainer.removeView(mapView) // 종료할 때 맵뷰 제거 (맵뷰 2개 이상 동시에 불가)
        super.finish()
    }
}