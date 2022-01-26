package com.app.service.parking.presentation.view.review.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.app.service.parking.R
import com.app.service.parking.custom.dialog.NaviBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.app.service.parking.databinding.ActivityReviewBinding
import com.app.service.parking.model.dto.Lot
import com.bumptech.glide.Glide


class ReviewBottomSheetDialog(var model: Lot? = null) : SuperBottomSheetFragment() {

    lateinit var binding: ActivityReviewBinding
    val viewModel: ReviewViewModel by viewModel()

    private var naviBottomSheetDialog: NaviBottomSheetDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // ViewModel의 주차장 모델 설정
        viewModel.lotModel = model
        // 주차장 코드로 즐겨찾기 모델 가져오기
        viewModel.getByParkCode()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.activity_review,
            container,
            false
        )
        model = null // 해당 Model은 ViewModel에 전달하였으므로 null로 초기화하여 메모리 절약

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingData() // 바인딩 데이터 세팅
        initView() // 뷰 초기화
    }

    private fun setBindingData() {
        binding.lifecycleOwner = this // Lifecycle 지정
        binding.model = viewModel.lotModel // 데이터바인딩 모델 세팅
        binding.viewModel = viewModel // viewModel 바인딩
    }

    private fun initView() {
        initMapView()
        with(binding) {
            // 즐겨찾기 여부에 따른 아이콘 설정
            viewModel?.isFavorite?.observe(requireActivity()) { isFavorite ->
                if (isFavorite) { // 즐겨찾기에 추가되어 있으면
                    Glide.with(requireContext()).load(R.drawable.ic_review_favorite)
                        .into(favoriteImageView)
                } else {
                    Glide.with(requireContext()).load(R.drawable.ic_review_unfavorite)
                        .into(favoriteImageView)
                }
            }

            // 즐겨찾기 Entity가 DB에 있으면 '즐겨찾기를 추가한 주차장 데이터'이므로
            // isFavorite를 true로 설정하고, 없으면 false로 설정한다.
            viewModel?.favoriteLot?.observe(requireActivity()) { favoriteEntity ->
                viewModel?.isFavorite?.value = (favoriteEntity != null)
            }

            favoriteButton.setOnClickListener {
                // 즐겨찾기가 추가되어 있는 경우
                if (viewModel?.isFavorite?.value == true) {
                    // 즐겨찾기 여부 변경
                    viewModel?.isFavorite?.value = false
                    // 즐겨찾기 해제
                    viewModel?.favoriteLot?.value?.let { entity -> viewModel?.deleteLot(entity) }
                } else { // 즐겨찾기 해제되어 있는 경우
                    // 즐겨찾기 여부 변경
                    viewModel?.isFavorite?.value = true
                    // 즐겨찾기 추가
                    viewModel?.insertLot()
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
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.clipboard_failed),
                            Toast.LENGTH_LONG
                        ).show() // 실패 토스트 출력
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
                // 클립보드 저장 성공 토스트 출력
                Toast.makeText(
                    requireContext(),
                    getString(R.string.clipboard_success),
                    Toast.LENGTH_LONG
                ).show()
            }
            // 내비게이션 버튼 클릭
            navigationButton.setOnClickListener {
                if (naviBottomSheetDialog == null) {
                    naviBottomSheetDialog = NaviBottomSheetDialog(requireActivity()).apply {
                        binding.dialog = this // 다이얼로그 데이터 바인딩
                        setAddress(model?.oldAddr)
                    }
                }
                dismiss() // Review Dialog 종료
                naviBottomSheetDialog?.show()
            }
            // 길찾기 버튼
            findRoadButton.setOnClickListener {
                val uri = Uri.parse("geo:${model?.latitude},${model?.longitude}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            if (model?.newAddr.isNullOrBlank()) {
                binding.parkingLotRoadNameTextView.visibility = View.GONE
            }
        }

    }

    private fun initMapView() {
        binding.kakaoMapContainer.visibility = View.GONE
    }
}