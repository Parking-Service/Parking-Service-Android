package com.app.buna.foodplace.feature.login

import android.os.Bundle
import androidx.activity.viewModels
import com.app.buna.foodplace.R
import com.app.buna.foodplace.databinding.ActivityLoginBinding
import com.app.buna.foodplace.feature.common.base.BaseActivity
import com.bumptech.glide.Glide

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val layoutResId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initActivity() {
        // gif background 설정
        Glide.with(this).load(R.drawable.bg_login_activity).into(getViewDataBinding().backgroundImageView)
    }
}