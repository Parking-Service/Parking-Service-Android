package com.app.service.parking.presentation.callback

import com.app.service.parking.presentation.view.login.type.LoginType
import com.app.service.parking.presentation.view.login.type.ResultType

interface ILoginCallback {
    fun onClickLoginButton(objects: Any?, loginType: LoginType) // 로그인 버튼을 눌렀을 때 호출
    fun onSignIn(loginType: LoginType, result: ResultType) // 로그인할 때 호출
}