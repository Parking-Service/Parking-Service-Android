package com.app.service.parking.custom.dialog

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.service.parking.R
import com.app.service.parking.databinding.LayoutNavChoiceBottomSheetBinding
import com.app.service.parking.extension.showToast
import com.arthurivanets.bottomsheets.BaseBottomSheet
import com.arthurivanets.bottomsheets.config.BaseConfig
import com.arthurivanets.bottomsheets.config.Config

class NaviBottomSheetDialog(
    hostActivity: Activity,
    config: BaseConfig = Config.Builder(hostActivity.baseContext).build()
) : BaseBottomSheet(hostActivity, config) {

    lateinit var binding: LayoutNavChoiceBottomSheetBinding
    private var address: String? = null

    // 패키지명
    private val kakaoNaviPackageName = "net.daum.android.map"
    private val naverNaviPackageName = "com.nhn.android.nmap"
    private val tMapNaviPackageName = "com.skt.tmap.ku"
    private val oneNaviPackageName = "kt.navi"

    override fun onCreateSheetContentView(context: Context): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_nav_choice_bottom_sheet,
            this,
            false
        )
        with(binding) {
            // 닫기 버튼 누르면 dismiss()
            closeButton.setOnClickListener {
                dismiss()
            }
        }
        return binding.root
    }

    fun setAddress(address: String?) {
        this.address = address
    }

    // 네비게이션 버튼 클릭
    fun onClickNaviButton(view: View) {
        val packageManager = context.packageManager
        var packageName: String? = null
        try {
            when (view.id) {
                R.id.kakao_navi_button -> {
                    packageName = kakaoNaviPackageName
                    startNavigation(packageManager, packageName)
                }
                R.id.naver_navi_button -> {
                    packageName = naverNaviPackageName
                    startNavigation(packageManager, packageName)
                }
                R.id.tmap_navi_button -> {
                    packageName = tMapNaviPackageName
                    startNavigation(packageManager, packageName)
                }
                R.id.one_navi_button -> {
                    packageName = oneNaviPackageName
                    startNavigation(packageManager, packageName)
                }
            }
        } catch (e: NullPointerException) { // 해당 내비게이션 앱이 없을 때 예외처리
            try { // 플레이스토어 앱 실행
                val playstoreIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName&referrer=${context.packageName}")
                )
                context.startActivity(playstoreIntent)
            }catch (e: ActivityNotFoundException) { // 플레이스토어가 없으면 웹으로 실행
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName&referrer=${context.packageName}")
                    )
                )
                context.showToast(context.getString(R.string.no_app))
            }
        }

        // 내비게이션 클릭시 다이얼로그 닫기
        dismiss()
    }

    private fun startNavigation(packageManager: PackageManager, packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
            ?.addCategory(Intent.CATEGORY_LAUNCHER)
        context.startActivity(intent)
    }

}