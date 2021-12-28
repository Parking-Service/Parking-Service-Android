package com.app.service.parking.feature.main.review

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityReviewWriteBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.repository.FavoriteRepository
import com.app.service.parking.util.PermissionHelper
import com.app.service.parking.feature.main.MainActivity

import com.werb.pickphotoview.PickPhotoView




class ReviewWriteActivity : BaseActivity<ActivityReviewWriteBinding, ReviewViewModel>() {

    override val layoutResId: Int = R.layout.activity_review_write
    override val viewModel: ReviewViewModel by lazy {
        ViewModelProvider(
            this,
            ReviewViewModel.Factory(FavoriteRepository(AppDB.getDatabase(this)))
        )[ReviewViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission() // 파일 읽기 권한이 있는지 확인
        initActivity()
    }

    override fun initActivity() {
        setBindingData()
        initView()
    }

    private fun setBindingData() {
        binding.viewModel = viewModel // ViewModel 바인딩
    }

    private fun initView() {
        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    // 파일 읽기 권한 확인
    private fun checkPermission() {
        PermissionHelper.checkReadExternalPermission(this@ReviewWriteActivity) {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> { // Toolbar의 Back키 눌렀을 때 동작
                finish() // 액티비티 종료
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PermissionHelper.PERMISSION_READ_EXTERNAL_REQUEST_CODE && grantResults.size == PermissionHelper.REQUIRED_PERMISSION_READ.size) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var isGranted = true

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                    break
                }
            }
            if (isGranted) { // 권한을 허용했다면
                // 갤러리 사진 피커 보여주기
                PickPhotoView.Builder(this)
                    .setPickPhotoSize(1) // select image size
                    .setClickSelectable(true) // click one image immediately close and return image
                    .setShowCamera(true) // is show camera
                    .setSpanCount(3) // span count
                    .setLightStatusBar(true) // lightStatusBar used in Android M or higher
                    .setStatusBarColor(R.color.white) // statusBar color
                    .setToolbarColor(R.color.white) // toolbar color
                    .setToolbarTextColor(R.color.black) // toolbar text color
                    .setSelectIconColor(R.color.colorPrimary) // select icon color
                    .setShowGif(false) // is show gif
                    .start()

            } else { // 사용자가 읽기 권한을 허용하지 않았다면
                // 사용자가 권한을 직접 거부한 경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        PermissionHelper.REQUIRED_PERMISSION_LOCATION[0]
                    )
                ) {
                    showToast(getString(R.string.permission_read_denied_1)) // 위치 권한이 거부되었다는 토스트 출력
                    finish() // 화면을 닫아 리뷰 작성 취소
                } else { // 사용자가 직접 권한을 거부한게 아닌, 수동적으로 거부된 상황이라면
                    showToast(getString(R.string.permission_read_denied_2))
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}