package com.app.buna.foodplace.model.dto

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid") var uid: String,
    @SerializedName("email") var email: String?=null,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("logintype") var logintype: String

) {
    override fun toString(): String {
        return "User(uid='$uid', email='$email', nickname='$nickname', loginType='$logintype')"
    }
}



