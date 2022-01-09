package com.app.service.parking.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Review(
    @Expose
    @SerializedName("reviewUid")
    val reviewUid: Int? = null,
    @Expose
    @SerializedName("reviewerUid")
    val reviewerUid: String? = null,
    @Expose
    @SerializedName("parkCode")
    val parkCode: String? = null,
    @Expose
    @SerializedName("reviewerNickName")
    val reviewerNickName: String? = null,
    @Expose
    @SerializedName("reviewRate")
    val reviewRate: Float? = null,
    @Expose
    @SerializedName("reviewDate")
    val reviewDate: Long? = null,
    @Expose
    @SerializedName("reviewText")
    val reviewText: String? = null,
    @Expose
    @SerializedName("likeCount")
    val likeCount: Short? = null,
    @Expose
    @SerializedName("reviewImageUrl")
    val reviewImageUrl: String? = null
)
