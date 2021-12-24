package com.app.service.parking.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose(serialize = true)
    @SerializedName("uid") var uid: String,
    @Expose(serialize = true)
    @SerializedName("email") var email: String?=null,
    @Expose(serialize = true)
    @SerializedName("nickname") var nickname: String,
    @Expose(serialize = true)
    @SerializedName("logintype") var logintype: String

) {
    override fun toString(): String {
        return "User(uid='$uid', email='$email', nickname='$nickname', loginType='$logintype')"
    }
}
