package com.app.service.parking.feature.main.review

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityReviewWriteBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.network.retrofit.builder.ReviewAPIBuilder.uploadReview
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.app.service.parking.model.type.RateStatus
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewWriteActivity : BaseActivity<ActivityReviewWriteBinding, ReviewWriteViewModel>() {

    override val layoutResId: Int = R.layout.activity_review_write
    override val viewModel: ReviewWriteViewModel by viewModel()
    val reviewViewModel: ReviewViewModel by viewModel()


    override fun initActivity() {
        setParkModel()
        setImagePicker()
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

            viewModel.reviewText.observe(this@ReviewWriteActivity) {
                reviewDoneButton.text =
                    getString(R.string.review_done_text, viewModel.reviewText.value?.length)
            }


            // EditText Hint에 닉네임 설정
            with(reviewEditText) {
                hint = getString(
                    R.string.rate_edit_text_hint, ParkingPreference.getString(
                        PreferenceConst.NICKNAME.name
                    )
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

    private fun setImagePicker() {
        // 갤러리에서 이미지 피커 실행
        TedImagePicker.with(this)
            .startMultiImage { uriList ->
                // 가져온 이미지 Uri로 업데이트
                viewModel.imgUriList.clear()
                viewModel.imgUriList.addAll(uriList)
                CoroutineScope(Dispatchers.IO).launch {
                    uploadReview(
                        Review(
                            userUid = "a11rEv1B1Ubi3yY7pbw53rtxyhm2",
                            parkCode = "000-1-000018",
                            name = "BuNa",
                            rate = 3,
                            created = 0L,
                            reviewText = "테스트",
                            likeCount = 123,
                            imgPath = uriList.get(0)
                        ), uriList.get(0)
                    )
                }
            }
    }

    override fun onBackPressed() {
        // 뒤로가기를 누르면 종료할 것인지 Alert Dialog로 질문+
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