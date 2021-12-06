package com.app.buna.foodplace.feature.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.buna.foodplace.feature.common.base.BaseViewModel
import com.app.buna.foodplace.feature.login.callback.ILoginCallback
import com.app.buna.foodplace.feature.login.type.LoginType
import com.app.buna.foodplace.feature.login.type.ResultType
import com.app.buna.foodplace.global.App
import com.app.buna.foodplace.model.dto.User
import com.app.buna.foodplace.model.repository.remote.UserRepository
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(val userRepository: UserRepository) : BaseViewModel() {

    private var loginCallback: ILoginCallback? = null
    var callbackManager = CallbackManager.Factory.create()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var isLoading = MutableLiveData<Boolean>(false) // 현재 로딩중인지 확인하는 live data
    var isSignedIn = MutableLiveData<Boolean>(false) // 로그인 되어있는지 확인하는 live data (true로 변경시 메인액티비티 실행하기 위한 필드)

    fun setLoginCallback(sessionCallback: ILoginCallback) {
        this.loginCallback = sessionCallback
    }


    /* * * * * * * * * / 
    * 카톡 로그인 시작 *
    * * * * * * * * * */
    
    fun loginWithKakao() {
        // 로그인을 로딩상태
        isLoading.value = true
        loginCallback?.onClickLoginButton(null, LoginType.KAKAO_TALK)
    }

    val kakaoLoginCallback : (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) { // 에러가 존재한다면 실패
            Timber.e("카카오 로그인 실패- ${error.cause}")
            if(error.cause == null) {
                loginCallback?.onSignIn(LoginType.KAKAO_TALK, ResultType.CANCELED)
            } else {
                loginCallback?.onSignIn(LoginType.KAKAO_TALK, ResultType.FAILURE)
            }
        } else if (token != null) { // 토큰이 비어있지 않다면 Success

            // 로그인 유저 정보 획득
            UserApiClient.instance.me { user, error -> // 회원가입 후에 로그인 성공시 로직 수행
                if(error == null) { // 에러가 존재하지 않을 경우 로그인되어 있는 유저 정보가 있음
                    viewModelScope.launch {
                        // repository를 통해 회원가입 요구(중복 계정일 경우 백엔드에서 처리)
                        userRepository.register(
                            User(
                                user?.id.toString(),
                                user?.kakaoAccount?.email,
                                user?.kakaoAccount?.profile?.nickname!!,
                                LoginType.KAKAO_TALK.name
                            )
                        )

                        Timber.d("Register completed, Trying Signining Kakao Talk ...")
                        loginCallback?.onSignIn(
                            LoginType.KAKAO_TALK,
                            ResultType.SUCCESS
                        ) // 다음 액티비티 실행을 위한 콜백 수행
                    }
                }
            }
        }
    }


    /* * * * * * * * * / 
    * 구글 로그인 시작 *
    * * * * * * * * * */
    fun loginWithGoogle() {
        // 로그인을 로딩상태
        isLoading.value = true

        // 사용자의 기본정보를 얻을 수 있는 객체 (userID, profile)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("949662077578-mohcbrov3i9f1k1ovsnq5017j6hb84h3.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // SignIn 클라이언트 객체 받아오기
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(App.context, gso)

        // 구글 로그인 화면을 띄우기 위한 startActivityForResult를 위해 view로 googleSignInClient객체 전달
        loginCallback?.onClickLoginButton(googleSignInClient, LoginType.GOOGLE)
    }

    // 구글 계정 로그인(회원가입) 메소드
    fun onGoogleSignInAccount(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            // 구글 로그인 성공시
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task -> // task : 사용자 및 결과 정보를 가지고 있음
                onLoginGoogleAuthTask(task) // 로그인 성공 후 수행할 메소드 호출
            }
        }
    }

    // 파이어베이스 구글 로그인 성공시 수행할 메소드
    private fun onLoginGoogleAuthTask(task: Task<AuthResult>) {
        viewModelScope.launch {
            if (task.isSuccessful) { // Google로 로그인 성공

                // repository를 통해 회원가입 요구(중복 계정일 경우 백엔드에서 처리)
                userRepository.register(User(auth.uid!!, auth.currentUser?.email, auth.currentUser?.displayName!!, LoginType.GOOGLE.name))

                Timber.d("Register completed, Trying Signining google...")
                isLoading.value = false // 로딩바 멈추기
                // 로그인 콜백을 호출하여 다음 액티비티 실행
                loginCallback?.onSignIn(LoginType.GOOGLE, ResultType.SUCCESS)

            } else { // Google로 로그인 실패
                Timber.e("Firebase Login Failure.")
            }
        }
    }


    /* * * * * * * * * / 
    * 페북 로그인 시작 *
    * * * * * * * * * */

    // 페이스북 로그인 버튼을 눌렀을 때 호출되는 메소드
    fun loginWithFacebook() {
        // 로그인을 로딩상태
        isLoading.value = true

        loginCallback?.onClickLoginButton(null, LoginType.FACEBOOK)
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    // 회원가입에 성공했을 경우
                    viewModelScope.launch {
                        onFacebookLogin(result?.accessToken)
                    }
                }
                override fun onCancel() { // 페이스북 로그인화면에서 뒤로가기를 누르는 등 취소시
                    loginCallback?.onSignIn(LoginType.FACEBOOK, ResultType.CANCELED) // 로그인 로딩 종료
                }
                override fun onError(error: FacebookException?) {
                    loginCallback?.onSignIn(LoginType.FACEBOOK, ResultType.FAILURE)
                }
            })
    }

    // 페이스북 로그인에 성공했을 때를 다루는 메소드
    private suspend fun onFacebookLogin(token : AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task -> // 만약 성공시 로그인 콜백을 호출하여 다음 액티비티로 이동
                viewModelScope.launch {
                    if (task.isSuccessful) { // 로그인 성공
                        // repository에게 회원가입 요청(중복 계정일 경우 백엔드에서 처리)
                        userRepository.register(User(auth.uid!!, auth.currentUser?.email, auth.currentUser?.displayName!!, LoginType.FACEBOOK.name))

                        Timber.d("Register completed, Trying Signining Facebook...")
                        loginCallback?.onSignIn(LoginType.FACEBOOK, ResultType.SUCCESS)
                    } else { // 로그인 실패
                        loginCallback?.onSignIn(LoginType.FACEBOOK, ResultType.FAILURE)
                    }
                }
            }
    }


    /* @카카오톡 로그인 되어 있는지 확인 */
    fun setIsKakaoSignIn() {
        UserApiClient.instance.me { user, _ ->
            isSignedIn.postValue((user != null)) // 유저 데이터가 존재하면, 로그인 되어있는 상태이다.
            Log.d("signIn", isSignedIn.toString())
        }
    }

    /* @ 파이어베이스용 계정(구글, 페이스북)이 로그인 되어 있는지 확인 */
    fun setIsFirebaseSignIn() {
        isSignedIn.value = (FirebaseAuth.getInstance() != null)
    }
    

}