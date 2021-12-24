package com.app.service.parking.model.dto

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("uid") var uid: Int,
    @SerializedName("parkCode") var parkCode: String,
    @SerializedName("nickname") var name: String,
    @SerializedName("rate") var rate: Short,
    @SerializedName("reviewDate") var created: Long,
    @SerializedName("review") var review: String,
    @SerializedName("likeCount") var likecount: Short,
    @SerializedName("img1") var imgPath1: String
)
