package com.app.service.parking.custom.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityReviewBinding
import com.app.service.parking.extension.showToast
import com.app.service.parking.feature.main.review.ReviewViewModel
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.entity.EntityFavorite
import com.app.service.parking.model.repository.local.repository.FavoriteRepository


class ReviewBottomSheetDialog(var model: Lot ?= null) : SuperBottomSheetFragment() {

    lateinit var binding: ActivityReviewBinding
    val viewModel: ReviewViewModel by lazy {
        ViewModelProvider(
            this,
            ReviewViewModel.Factory(FavoriteRepository(AppDB.getDatabase(requireContext())))
        )[ReviewViewModel::class.java]
    }
    private var naviBottomSheetDialog: NaviBottomSheetDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.lotModel = model
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.activity_review,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.model = viewModel.lotModel // 데이터바인딩 모델 세팅
        initView()
        model = null // 해당 Model은 ViewModel에 전달하였으므로 null로 초기화하여 메모리 절약

        return binding.root
    }


    private fun initView() {
        initMapView()
        with(binding) {
            favoriteButton.setOnClickListener {
                with(viewModel?.lotModel) {
                    viewModel?.insertFavoriteLot(
                        EntityFavorite(
                            this?.parkCode!!,
                            parkName,
                            newAddr,
                            oldAddr,
                            operDay,
                            weekdayOpenTime,
                            weekdayCloseTime,
                            saturdayOpenTime,
                            saturdayCloseTime,
                            holidayOpenTime,
                            holidayCloseTime,
                            feeType,
                            basicParkTime,
                            basicFee,
                            addUnitTime,
                            addUnitFee,
                            parkTimePerDay,
                            feePerDay,
                            feePerMonth,
                            payType,
                            uniqueness,
                            phoneNumber,
                            latitude,
                            longitude
                        )
                    )
                }
            }

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
                    requireContext().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager

                var text = when {
                    model?.newAddr.isNullOrBlank().not() -> {
                        model?.newAddr
                    }
                    model?.oldAddr.isNullOrBlank().not() -> {
                        model?.oldAddr
                    }
                    else -> {
                        // 주차장 주소가 없음
                        requireContext().showToast(getString(R.string.clipboard_failed)) // 실패 토스트 출력
                        return@setOnClickListener
                    }
                }
                // 클리보드 데이터 객체 생성
                val clipData = ClipData.newPlainText(
                    getString(R.string.clipboard_label),
                    text
                )
                // 클립보드에 저장
                clipboard.setPrimaryClip(clipData)
                // 성공 토스트 출력
                requireContext().showToast(getString(R.string.clipboard_success))
            }
            // 내비게이션 버튼 클릭
            navigationButton.setOnClickListener {
                if (naviBottomSheetDialog == null) {
                    naviBottomSheetDialog = NaviBottomSheetDialog(requireActivity()).apply {
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

            if(model?.newAddr.isNullOrBlank()) {
                binding.parkingLotRoadNameTextView.visibility = View.GONE
            }
        }
    }

    /*override fun onBackPressed() {
        // 내비게이션 선택 다이얼로그가 열려있으면
        if (naviBottomSheetDialog?.isShown == true) {
            naviBottomSheetDialog?.dismiss() // 다이얼로그를 닫고,
        } else { // 아무것도 열려있지 않으면
            super.onBackPressed() // 뒤로가기
        }
    }*/

    override fun getCornerRadius() = requireContext().resources.getDimension(R.dimen.demo_sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED

    private fun initMapView() {
        binding.kakaoMapContainer.visibility = View.GONE
    }
}