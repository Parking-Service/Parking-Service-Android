package com.app.service.parking.model.network.retrofit.service

import android.util.Log
import com.app.service.parking.model.dto.User
import com.app.service.parking.model.network.retrofit.api.UserAPI
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object UserAPIService : BaseRetrofitService() {

    /* POST 방식 */
    // 회원가입
    suspend fun register(user: User) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val api = getRetrofit().create(UserAPI::class.java)
            api.getRegisterResponse(user).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.body() != null && response.isSuccessful) {
                        Timber.tag("User Register Response").d("Success")
                        Log.d("User Register Response", "${response.body()}")
                        continuation.resume(Unit, null)
                    } else {
                        val error = response.errorBody()?.string()
                        if(error == "100") {
                            Timber.tag("User Register Response").d("Failed by error 100")
                        } else {
                            Timber.tag("User Register Response").d("Failed by other")
                        }
                        continuation.resume(Unit, null)
                    }

                    Log.d("Coroutine", "register response")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Timber.tag("User Register Response").d("Failed by : ${t.message}")
                    continuation.resume(Unit, null)
                }
            })
        }
    }

}