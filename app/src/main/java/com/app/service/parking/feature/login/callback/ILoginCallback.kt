package com.app.service.parking.feature.login.callback

import com.app.service.parking.feature.login.type.LoginType
import com.app.service.parking.feature.login.type.ResultType

interface ILoginCallback {
    fun onClickLoginButton(`object`: Any?, loginType: LoginType) // 로그인 버튼을 눌렀을 때 호출
    fun onSignIn(loginType: LoginType, result: ResultType) // 로그인할 때 호출
}