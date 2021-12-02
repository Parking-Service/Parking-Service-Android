package com.app.buna.foodplace.feature.login.callback

import com.app.buna.foodplace.feature.login.type.LoginType
import com.app.buna.foodplace.feature.login.type.ResultType

interface ILoginCallback {
    fun onClickLoginButton(client: Any?, loginType: LoginType) // 로그인 버튼을 눌렀을 때 호출
    fun onSignIn(loginType: LoginType, result: ResultType) // 로그인할 때 호출
}