package com.app.service.parking.feature.main.review

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityReviewWriteBinding
import com.app.service.parking.databinding.ViewPickedPhotoBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.app.service.parking.model.type.RateStatus
import com.bumptech.glide.Glide
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewWriteActivity : BaseActivity<ActivityReviewWriteBinding, ReviewWriteViewModel>() {

    override val layoutResId: Int = R.layout.activity_review_write
    override val viewModel: ReviewWriteViewModel by viewModel()
    val reviewViewModel: ReviewViewModel by viewModel()


    override fun initActivity() {
        setParkModel()
        showImagePicker()
        setBindingData()
        initView()
    }

    private fun setBindingData() {
        binding.reviewWriteViewModel = viewModel // ReviewWriteViewModel 바인딩
        binding.reviewViewModel = reviewViewModel // ReviewViewModel 바인딩
        binding.rateStatus = viewModel.rateStatus.value // 리뷰 상태 바인딩
    }

    private fun setParkModel() {
        // Search Activity에서 검색 데이터로 받은 주차장 모델로 초기화
        reviewViewModel.lotModel = intent.getSerializableExtra("model") as Lot
    }

    private fun initView() {
        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

            // 리뷰 텍스트 관찰
            viewModel.reviewText.observe(this@ReviewWriteActivity) { text ->
                val reviewLength = text.length // 리뷰 길이
                reviewDoneButton.text =
                    getString(R.string.review_done_text, reviewLength) // 리뷰 내용이 변함에 따라 현재 글자수 변경
                // 글자수가 1글자 이상일 때 버튼 활성화, 내용이 없으면 비활성화
                reviewDoneButton.isEnabled = reviewLength > 0
                reviewDoneButton.isClickable = reviewLength > 0
            }

            // 이미지 리스트 라이브 데이터를 관찰한다.
            viewModel.imgUriLiveList.observe(this@ReviewWriteActivity) { uriList ->
                // 데이터에 변동이 있다면, 이미지 현재 개수로 갱신
                photoCountText.text = "${uriList.size} / ${viewModel.imageOriginMaxCount}"
            }
            
            viewModel.isUploadSuccess.observe(this@ReviewWriteActivity) { isSuccess ->
                uploadProgressBar.visibility = View.GONE // 결과 반환시 프로그레스바 사라지게 하기
                if(isSuccess) { // 업로드 성공시
                    // 성공 문구 출력 후, 액티비티 종료
                    showToast(getString(R.string.review_upload_success))
                    finish()
                }else { // 업로드 실패시
                    // 실패 문구 출력
                    showToast(getString(R.string.review_upload_failed))
                }
            }

            // 사진 추가 버튼 클릭시
            photoAddButton.setOnClickListener {
                showImagePicker() // 이미지 피커를 보여준다.
            }

            // 작성 완료 버튼 클릭시
            reviewDoneButton.setOnClickListener {
                uploadProgressBar.visibility = View.VISIBLE // 프로그레스 바를 보여준다.
                reviewViewModel?.lotModel?.let { lot -> reviewWriteViewModel?.uploadReview(lot) } // 서버에 리뷰를 등록한다.
            }

            // EditText Hint에 닉네임 설정
            with(reviewEditText) {
                hint = getString(
                    R.string.rate_edit_text_hint, ParkingPreference.getString(
                        PreferenceConst.NICKNAME.name
                    ) ?: getString(R.string.default_user_nickname)
                )

                // Text Watcher 설정
                addTextChangedListener(object : TextWatcher {
                    // 입력하기 전에 호출
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    // 타이핑되는 텍스트에 변화가 있으면 호출
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        viewModel.reviewText.value = s.toString()
                    }

                    // 입력이 끝날 때 호출
                    override fun afterTextChanged(s: Editable?) {}
                })
            }

            // 리뷰 상태 관찰
            viewModel.rateStatus.observe(this@ReviewWriteActivity) { rateStatus ->
                when (rateStatus) {
                    // 좋아요 누른경우
                    RateStatus.GOOD -> {
                        // 좋아요 버튼 활성화
                        rateGoodText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        rateGoodImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        // 나머지 버튼 비활성화
                        rateNormalText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateNormalImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateBadText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateBadImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                    }
                    // 평범해요 누른 경우
                    RateStatus.NORMAL -> {
                        // 평범해요 버튼 활성화
                        rateNormalText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        rateNormalImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        // 나머지 버튼 비활성화
                        rateGoodText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateGoodImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateBadText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateBadImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                    }
                    // 별로에요 누른 경우
                    RateStatus.BAD -> {
                        // 별로에요 버튼 활성화
                        rateBadText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        rateBadImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.colorPrimary
                            )
                        )
                        // 나머지 버튼 비활성화
                        rateNormalText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateNormalImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateGoodText.setTextColor(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                        rateGoodImage.imageTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@ReviewWriteActivity,
                                R.color.unselectedRateColor
                            )
                        )
                    }
                }
            }
        }
    }

    private fun showImagePicker() {
        // 이미지 최대 선택개수
        val originMaxCount = viewModel.imageOriginMaxCount // 이미지 원래 최대개수
        val imgMaxCount =
            originMaxCount - (viewModel.imgUriList.size) // 현재 선택한 이미지 내에서, 더 선택할 수 있는 이미지 개수

        // 이미지 개수가 더 선택할 수 있는 값이라면
        if (imgMaxCount > 0) {
            // 갤러리에서 이미지 피커 실행
            TedImagePicker.with(this)
                .max(imgMaxCount, getString(R.string.image_max_toast, imgMaxCount))
                .startMultiImage { uriList ->
                    // 가져온 이미지 Uri로 업데이트
                    //viewModel.imgUriList.clear() // 기존 이미지 Uri 리스트 초기화
                    viewModel.imgUriList.addAll(uriList) // 새로운 이미지 Uri 추가
                    viewModel.imgUriLiveList.value = viewModel.imgUriList // 리스트 라이브 데이터 데이터 변경

                    uriList.forEach { imgUri ->
                        // 이미지 Uri을 사용하여 포토 바인딩 객체 생성
                        with(
                            ViewPickedPhotoBinding.inflate(
                                layoutInflater,
                                binding.scrollInnerView,
                                false
                            )
                        ) {
                            Glide.with(this@ReviewWriteActivity).load(imgUri)
                                .into(this.photoImageView) // Uri
                            this.photoDeleteButton.setOnClickListener { // 삭제 버튼 클릭시
                                viewModel.imgUriList.remove(imgUri) // 이미지 리스트에서 해당 Uri 제거
                                binding.scrollInnerView.removeView(this.root) // 스크롤뷰에서 이미지 제거
                                viewModel.imgUriLiveList.value =
                                    viewModel.imgUriList // 리스트 라이브 데이터 데이터 변경
                            }
                            binding.scrollInnerView.addView(this.root) // 스크롤뷰 내부의 LinearLayout에 PhotoView를 추가한다.
                        }

                    }
                }
        } else { // 더 이상 선택하지 못하는 경우 (이미지를 최대개수만큼 선택함)
            showToast(getString(R.string.image_max_toast, originMaxCount))
        }
    }

    override fun onBackPressed() {
        // 뒤로가기를 누르면 종료할 것인지 Alert Dialog로 질문
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.write_review_alert_title))
            .setMessage(getString(R.string.write_review_alert_message))
            .setPositiveButton(
                getString(R.string.ok)
            ) { _, _ -> finish() }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // Toolbar의 Back키 눌렀을 때 동작
                finish() // 액티비티 종료
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}