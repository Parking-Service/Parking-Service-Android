package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.Review
import retrofit2.Call
import retrofit2.http.*

interface ReviewAPI {
    // 리뷰 저장
    @POST("/review/post")
    fun putReview(@Body review: Review): Call<Void>

    // 리뷰 업데이트
    @PUT("/review/update/{id}")
    fun updateReview(@Path("id") id: Int): Call<Void>

    // 리뷰 리스트 가져오기
    @GET("/review")
    fun getReviewList(@Query("parkcode") parkCode: String): Call<List<Review>>

    // 리뷰 삭제
    @DELETE("/review/remove/{id}")
    fun deleteReview(@Path("id") id: Int): Call<Void>
}