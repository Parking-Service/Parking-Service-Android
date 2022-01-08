package com.app.service.parking.model.dto

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("reviewUid") var reviewUid: Int? = null,
    @SerializedName("reviewerUid") var userUid: String? = null,
    @SerializedName("parkCode") var parkCode: String? = null,
    @SerializedName("reviewerName") var name: String? = null,
    @SerializedName("reviewRate") var rate: Short? = null,
    @SerializedName("reviewDate") var created: Long? = null,
    @SerializedName("reviewText") var reviewText: String? = null,
    @SerializedName("likeCount") var likeCount: Short? = null,
    @SerializedName("reviewImageUrl") var imgPaths: List<String>? = null
)
