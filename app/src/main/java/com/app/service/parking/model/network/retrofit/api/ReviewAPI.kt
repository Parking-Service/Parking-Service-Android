package com.app.service.parking.model.network.retrofit.api

import com.app.service.parking.model.dto.Review
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ReviewAPI {
    // 리뷰 저장
    // 이미지를 리스트로 등록하는 경우
    @Multipart
    @POST("/review/upload")
    fun uploadReview(
        @Part("uid") reviewerUid: String?,
        @Part("parkCode") parkCode: String?,
        @Part imgList: ArrayList<MultipartBody.Part>?,
        @Part("text") reviewText: String?,
        @Part("rate") reviewRate: Float?
    ): Call<Void>

    // 리뷰 저장
    // 이미지를 하나 등록하는 경우
    @Multipart
    @POST("/review/upload")
    fun uploadReview(
        @Part("uid") reviewerUid: String?,
        @Part("parkCode") parkCode: String?,
        @Part imgList: MultipartBody.Part?,
        @Part("text") reviewText: String?,
        @Part("rate") reviewRate: Float?
    ): Call<Void>

    // 리뷰 업데이트
    @FormUrlEncoded
    @Multipart
    @PUT("/review/update")
    fun updateReview(
        @Query("reviewUid") id: Int?,
        @Part reviewImgs: MultipartBody.Part?,
        @Part("text") reviewText: String?,
        @Part("rate") reviewRate: Float?
    ): Call<Void>

    // 리뷰 리스트 가져오기
    @GET("/review")
    fun getBestReviewList(@Query("parkCode") parkCode: String): Call<List<Review>>

    // 리뷰 리스트 가져오기
    @GET("/review/all")
    fun getAllReviewList(@Query("parkCode") parkCode: String): Call<List<Review>>

    // 특정 리뷰 가져오기
    @GET("/review")
    fun getReview(@Query("reviewUid") reviewUid: String): Call<List<Review>>

    // 리뷰 삭제
    @DELETE("/review/remove")
    fun deleteReview(@Query("reviewUid") id: Int): Call<Void>
}