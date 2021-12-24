package com.app.service.parking.model.network.retrofit.builder

import android.util.Log
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.dto.User
import com.app.service.parking.model.network.retrofit.api.ReviewAPI
import com.app.service.parking.model.network.retrofit.api.UserAPI
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object ReviewAPIBuilder : BaseRetrofitBuilder() {



    /* POST 방식
     서버에 리뷰 저장
     */
    suspend fun putReview(review: Review) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val api = getRetrofit().create(ReviewAPI::class.java)
            api.putReview(review).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.body() != null && response.isSuccessful) {
                        Timber.tag("User Register Response").d("Success")
                        Log.d("User Register Response", "${response.body()}")
                    } else {
                        val error = response.errorBody()?.string()
                        if(error == "100") {
                            Timber.tag("User Register Response").d("Failed by error 100")
                        } else {
                            Timber.tag("User Register Response").d("Failed by other")
                        }
                    }
                    continuation.resume(Unit, null)
                    Log.d("Coroutine", "register response")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Timber.tag("User Register Response").d("Failed")
                    Log.d("Coroutine", "Retrofit")
                    continuation.resume(Unit, null)
                }
            })
        }
    }



    /* PUT 방식
     서버에 저장된 리뷰 데이터 업데이트
     */
    suspend fun updateReview(reviewId: Int) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val api = getRetrofit().create(ReviewAPI::class.java)
            api.updateReview(reviewId).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.body() != null && response.isSuccessful) {
                        Timber.tag("User Register Response").d("Success")
                        Log.d("User Register Response", "${response.body()}")
                    } else {
                        val error = response.errorBody()?.string()
                        if(error == "100") {
                            Timber.tag("User Register Response").d("Failed by error 100")
                        } else {
                            Timber.tag("User Register Response").d("Failed by other")
                        }
                    }
                    continuation.resume(Unit, null)
                    Log.d("Coroutine", "register response")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Timber.tag("User Register Response").d("Failed")
                    Log.d("Coroutine", "Retrofit")
                    continuation.resume(Unit, null)
                }
            })
        }
    }


    /* GET 방식
     서버로부터 주차장 코드를 바탕으로 리뷰 리스트 요청
     */
    suspend fun getReviewList(parkCode: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val api = getRetrofit().create(ReviewAPI::class.java)
            api.getReviewList(parkCode).enqueue(object :
                Callback<List<Review>> {
                override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                    if(response.body() != null && response.isSuccessful) {
                        Timber.tag("User Register Response").d("Success")
                        Log.d("User Register Response", "${response.body()}")
                    } else {
                        val error = response.errorBody()?.string()
                        if(error == "100") {
                            Timber.tag("User Register Response").d("Failed by error 100")
                        } else {
                            Timber.tag("User Register Response").d("Failed by other")
                        }
                    }
                    continuation.resume(Unit, null)
                    Log.d("Coroutine", "register response")
                }

                override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                    Timber.tag("User Register Response").d("Failed")
                    Log.d("Coroutine", "Retrofit")
                    continuation.resume(Unit, null)
                }
            })
        }
    }



    /* DELETE 방식
     서버에 저장된 리뷰 데이터 삭제
     */
    suspend fun deleteReview(id: Int) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val api = getRetrofit().create(ReviewAPI::class.java)
            api.deleteReview(id).enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.body() != null && response.isSuccessful) {
                        Timber.tag("User Register Response").d("Success")
                        Log.d("User Register Response", "${response.body()}")
                    } else {
                        val error = response.errorBody()?.string()
                        if(error == "100") {
                            Timber.tag("User Register Response").d("Failed by error 100")
                        } else {
                            Timber.tag("User Register Response").d("Failed by other")
                        }
                    }
                    continuation.resume(Unit, null)
                    Log.d("Coroutine", "register response")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Timber.tag("User Register Response").d("Failed")
                    Log.d("Coroutine", "Retrofit")
                    continuation.resume(Unit, null)
                }
            })
        }
    }


}