package com.app.service.parking.feature.main.review

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.app.service.parking.R
import com.app.service.parking.custom.NaviBottomSheetDialog
import com.app.service.parking.databinding.ActivityReviewBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.model.dto.Lot
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewActivity : BaseActivity<ActivityReviewBinding, ReviewViewModel>() {

    override val layoutResId: Int = R.layout.activity_review
    override val viewModel: ReviewViewModel by viewModel()
    lateinit var mapView: MapView // 카카오 맵 뷰
    var naviBottomSheetDialog: NaviBottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setParkModel() // Intent로 받은 주차장 데이터로 초기화
        initView() // 뷰 초기화
    }

    override fun initActivity() {
        mapView = MapView(this) // 카카오 맵 뷰 초기화
        binding.kakaoMapContainer.addView(mapView) // 지도란에 카카오 맵 뷰 추가
    }

    override fun finish() {
        binding.kakaoMapContainer.removeView(mapView) // 종료할 때 맵뷰 제거 (맵뷰 2개 이상 동시에 불가)
        super.finish()
    }

    private fun initView() {
        with(binding) {
            // 전화버튼 클릭리스너
            callButton.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("tel:${model?.phoneNumber}")
                    )
                )
            }
            // 주소 복사 버튼 클릭
            copyAddressButton.setOnClickListener {
                val clipboard: ClipboardManager =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(
                    getString(R.string.clipboard_label),
                    model?.newAddr ?: model?.oldAddr
                )
                clipboard.setPrimaryClip(clipData)
                showToast(getString(R.string.clipboard_success))
            }
            // 내비게이션 버튼 클릭
            navigationButton.setOnClickListener {
                if (naviBottomSheetDialog == null) {
                    naviBottomSheetDialog = NaviBottomSheetDialog(this@ReviewActivity).apply {
                        binding.dialog = this // 다이얼로그 데이터 바인딩
                        setAddress(model?.oldAddr)
                    }
                }
                naviBottomSheetDialog?.show()
            }
            // 길찾기 버튼
            findRoadButton.setOnClickListener {
                val uri = Uri.parse("geo:${model?.latitude},${model?.longitude}")
                val intent = Intent (Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        // 내비게이션 선택 다이얼로그가 열려있으면
        if (naviBottomSheetDialog?.isShown == true) {
            naviBottomSheetDialog?.dismiss() // 다이얼로그를 닫고,
        } else { // 아무것도 열려있지 않으면
            super.onBackPressed() // 뒤로가기
        }
    }

    private fun setParkModel() {
        viewModel.lotModel = intent.getSerializableExtra("model") as Lot
        binding.model = viewModel.lotModel // 데이터바인딩 모델 세팅
    }
}