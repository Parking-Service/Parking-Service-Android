package com.app.service.parking.model.dto

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("reviewUid") var reviewUid: Int? = null,
    @SerializedName("reviewerUid") var userUid: String?,
    @SerializedName("parkCode") var parkCode: String?,
    @SerializedName("reviewerNickName") var name: String?,
    @SerializedName("reviewRate") var rate: Short?,
    @SerializedName("reviewDate") var created: Long?,
    @SerializedName("reviewText") var reviewText: String?,
    @SerializedName("likeCount") var likecount: Short?,
    @SerializedName("reviewImageUrl") var imgPath: String?
)
