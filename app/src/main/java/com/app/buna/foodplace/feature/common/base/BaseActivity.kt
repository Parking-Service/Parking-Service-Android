package com.app.buna.foodplace.feature.common.base

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.buna.foodplace.R
import com.app.buna.foodplace.util.NetworkConnection
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<T: ViewDataBinding, S: BaseViewModel> : AppCompatActivity() {
    
    private lateinit var viewDataBinding: T

    abstract val layoutResId: Int
    abstract val viewModel: S
    abstract fun initActivity()
    private var networkStatusSnackbar: Snackbar? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 초기화된 layoutResId로 data binding 객체 생성
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
        // Live data를 사용하기 위한 lifecycleOwner 지정
        viewDataBinding.lifecycleOwner = this@BaseActivity

        initActivity()

    }

    override fun onResume() {
        super.onResume()
        // 인터넷 연결 상태 실시간 확인
        val networkConnection = NetworkConnection(applicationContext)

        networkConnection.observe(this, { isConnected -> // LiveData를 통해 실시간으로 네트워크 상태 확인
            if(isConnected) { // 인터넷 연결이 완료되었다면
                dismissInternetCheckDialog() // 네트워크 확인 다이얼로그 dismiss()
            } else{ // 인터넷 연결이 끊겼다면
                createInternetCheckDialog() // 네트워크 확인 다이얼로그 생성
                showInternetCheckDialog() // 네트워크 확인 다이얼로그 show()
            }
        })
    }

    // binding 객체를 가져오는 메소드
    fun getViewDataBinding(): T = viewDataBinding

    private fun createInternetCheckDialog() {
        val snackbar = Snackbar.make(viewDataBinding.root, getString(R.string.snack_bar_content_internet_disconnected), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.ok)) { snackbar.dismiss() }
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant))
            .setBackgroundTint(ContextCompat.getColor(this, R.color.snackbarBackground))
            .show()
    }

    // 인터넷 연결 상태 확인을 위한 다이얼로그를 띄우는 메소드
    private fun showInternetCheckDialog() {
        networkStatusSnackbar?.show()
    }

    // 인터넷 연결 상태 확인을 위한 다이얼로그를 닫는 메소드
    private fun dismissInternetCheckDialog() {
        networkStatusSnackbar?.dismiss()
    }
}