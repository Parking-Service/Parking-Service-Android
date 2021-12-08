package com.app.service.parking.model.repository.remote

import com.app.service.parking.model.dto.User
import com.app.service.parking.model.network.retrofit.builder.RetrofitUserAPIBuilder

class UserRepository {

    // @회원가입한 유저의 정보를 스프링부트 서버에 저장
    suspend fun register(user: User) {
        RetrofitUserAPIBuilder.register(user)
    }

}