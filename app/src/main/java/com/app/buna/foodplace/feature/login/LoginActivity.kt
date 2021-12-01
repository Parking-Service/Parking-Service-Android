package com.app.buna.foodplace.feature.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.app.buna.foodplace.R
import com.app.buna.foodplace.databinding.ActivityLoginBinding
import com.app.buna.foodplace.feature.common.base.BaseActivity
import com.app.buna.foodplace.feature.login.callback.ILoginCallback
import com.app.buna.foodplace.feature.login.type.LoginType
import com.app.buna.foodplace.feature.login.type.ResultType
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), ILoginCallback {

    override val layoutResId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModels()
    private val binding: ActivityLoginBinding by lazy {
        getViewDataBinding()!!
    }
    private lateinit var googleLoginResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initActivity() {
        binding.viewModel = viewModel
        viewModel.setSessionCallback(this)
        // gif background 설정
        Glide.with(this).load(R.drawable.bg_login_activity).into(binding.backgroundImageView)

        // start activity result
        googleLoginResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val signInTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = signInTask.getResult(ApiException::class.java)
                    viewModel.onGoogleSignInAccount(account)
                } catch (e: ApiException) {
                    Timber.e(e.message)
                }
            }
        }
    }


    override fun onClickLoginButton(others: Any?, loginType: LoginType) {
        when (loginType) {
            LoginType.GOOGLE -> signInWithGoogle(others as GoogleSignInClient)
            LoginType.FACEBOOK -> LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
            LoginType.KAKAO_TALK -> {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) { // 카카오톡이 설치되어 있는 경우
                    // 카카오톡을 통한 회원가입 화면 실행
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = viewModel.kakaoLoginCallback)
                } else { // 카카오톡이 설치되어 있지 않은 경우
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = viewModel.kakaoLoginCallback)
                }
            }
        }
    }

    // googleSignInClient와 같은 로그인에 필요한 '객체'를 전달받기 위한 콜백
    override fun onSignIn(loginType: LoginType, result: ResultType) {
        if (result == ResultType.FAILURE) {
            showToast(getString(R.string.fail_login))
            return
        }
        when (loginType) {
            LoginType.GOOGLE -> {
            }
            LoginType.FACEBOOK -> {
            }
            LoginType.KAKAO_TALK -> {
            }
        }
        
        // 로그인 다음 화면 실행
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // 구글 로그인 화면 출력
    private fun signInWithGoogle(googleSignInClient: GoogleSignInClient) {
        googleSignInClient.signInIntent.run {
            googleLoginResultLauncher.launch(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}