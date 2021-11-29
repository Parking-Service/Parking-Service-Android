package com.app.buna.foodplace.feature.common.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T: ViewDataBinding, R: BaseViewModel> : AppCompatActivity() {
    
    private lateinit var viewDataBinding: T

    abstract val layoutResId: Int
    abstract val viewModel: R
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 초기화된 layoutResId로 data binding 객체 생성
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
        // Live data를 사용하기 위한 lifecycleOwner 지정
        viewDataBinding.lifecycleOwner = this@BaseActivity
    }

    fun getViewDataBinding(): T = viewDataBinding
}