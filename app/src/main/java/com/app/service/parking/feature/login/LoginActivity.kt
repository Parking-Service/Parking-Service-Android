package com.app.service.parking.feature.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityLoginBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.feature.login.callback.ILoginCallback
import com.app.service.parking.feature.login.type.LoginType
import com.app.service.parking.feature.login.type.ResultType
import com.app.service.parking.feature.main.MainActivity
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.preference.PreferenceConst
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.user.UserApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(), ILoginCallback {

    override val layoutResId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel by viewModel()
    private lateinit var googleLoginResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewModel의 isLoading을 옵저빙
        viewModel.isLoading.observe(this, { isLoading ->
            // 로딩중이면 버튼 잠금, 로딩끝나면 버튼 잠금 해제
            isLoading.not().let {
                binding.facebookLoginButton.isClickable = it
                binding.kakaoLoginButton.isClickable = it
                binding.googleLoginButton.isClickable = it
                binding.skipTextButton.isClickable = it
                
                if(it) { // 로딩이 끝났으면 프로그레스바 안보이게
                    binding.loginProgressBar.visibility = View.GONE
                }else { // 로딩중이면 프로그레스바 보이게
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
                 
            }
        })

        // 건너뛰기 버튼
        binding.skipTextButton.setOnClickListener {
            startMainActivity()
        }
    }

    override fun onStart() {
        super.onStart()

        // 만약 로그인 되어있다면 바로 메인 액티비티 실행
        viewModel.isSignedIn.observe(this, { isSignIn ->
            if(isSignIn) {
                startMainActivity()
            }
        })

        when(ParkingPreference.getString(PreferenceConst.LOGIN_TYPE.name)) {
            LoginType.FACEBOOK.name, LoginType.GOOGLE.name -> { // 마지막 로그인 타입이 페이스북 또는 구글로 되어 있는 경우
                viewModel.setIsFirebaseSignIn() // 파이어베이스 Auth로 로그인 되어 있는지 확인
            } LoginType.KAKAO_TALK.name -> { // 마지막 로그인 타입이 카카오톡이라면
                viewModel.setIsKakaoSignIn() // 카카오톡으로 로그인 되어 있는지 확인
            }
        }
    }

    override fun initActivity() {
        binding.viewModel = viewModel
        viewModel.setLoginCallback(this)

        // gif background 설정
        Glide.with(this).load(R.drawable.bg_login_activity).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(binding.backgroundImageView)

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
            viewModel.isLoading.value = false // 로그인 로딩 종료
        }
    }


    override fun onClickLoginButton(others: Any?, loginType: LoginType) {
        when (loginType) {
            LoginType.GOOGLE -> signInWithGoogle(others as GoogleSignInClient)
            LoginType.FACEBOOK -> LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
            LoginType.KAKAO_TALK -> {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) { // 카카오톡이 설치되어 있는 경우
                    // 카카오톡 앱을 통한 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = viewModel.kakaoLoginCallback)
                } else { // 카카오톡이 로그인 있지 않은 경우
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = viewModel.kakaoLoginCallback)
                }
            }
        }
    }

    // googleSignInClient와 같은 로그인에 필요한 '객체'를 전달받기 위한 콜백
    override fun onSignIn(loginType: LoginType, result: ResultType) {
        if(result == ResultType.CANCELED) {
            Log.d("onSignIn","로그인 취소")
            viewModel.isLoading.value = false // 로그인 로딩 종료
            return
        }
        if (result == ResultType.FAILURE) {
            showToast(getString(R.string.fail_login))
            viewModel.isLoading.value = false // 로그인 로딩 종료
            return
        }
        when (loginType) {
            LoginType.GOOGLE -> {
                ParkingPreference.putValue(PreferenceConst.LOGIN_TYPE.name, LoginType.GOOGLE.name)
            }
            LoginType.FACEBOOK -> {
                ParkingPreference.putValue(PreferenceConst.LOGIN_TYPE.name, LoginType.FACEBOOK.name)
            }
            LoginType.KAKAO_TALK -> {
                ParkingPreference.putValue(PreferenceConst.LOGIN_TYPE.name, LoginType.KAKAO_TALK.name)
            }
        }
        viewModel.isLoading.value = false // 로그인 로딩 종료
        viewModel.isSignedIn.value = true // 로그인 상태 변경

    }

    // 구글 로그인 화면 출력
    private fun signInWithGoogle(googleSignInClient: GoogleSignInClient) {
        googleSignInClient.signInIntent.run {
            googleLoginResultLauncher.launch(this)
        }
    }

    private fun startMainActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}