package com.app.buna.foodplace.feature.main.review

import android.os.Bundle
import com.app.buna.foodplace.R
import com.app.buna.foodplace.databinding.ActivityReviewBinding
import com.app.buna.foodplace.feature.common.base.BaseActivity
import com.app.buna.foodplace.model.network.retrofit.builder.RetrofitParkingAPIBuilder
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewActivity : BaseActivity<ActivityReviewBinding, ReviewViewModel>() {

    override val layoutResId: Int = R.layout.activity_review
    override val viewModel: ReviewViewModel by viewModel()
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitParkingAPIBuilder.getParkingLots()
    }

    override fun initActivity() {
        mapView = MapView(this)
        val mapViewContainer = binding.kakaoMapContainer
        mapViewContainer.addView(mapView)
    }
}