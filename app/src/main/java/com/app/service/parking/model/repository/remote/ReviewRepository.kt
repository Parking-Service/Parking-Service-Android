package com.app.service.parking.model.repository.remote

import android.net.Uri
import com.app.service.parking.model.dto.Review
import com.app.service.parking.model.network.retrofit.builder.ReviewAPIBuilder

class ReviewRepository {

    // @작성한 리뷰를 서버에 업로드한다.
    // 반환값 성공 실패 여부 (Boolean)
    suspend fun upload(review: Review, uri: List<Uri>): Boolean =
        ReviewAPIBuilder.uploadReview(review, uri)

    // 주차 코드를 바탕으로 서버로부터 리스트 요청
    suspend fun getReviewList(parkCode: String): ArrayList<Review> =
        ReviewAPIBuilder.getReviewList(parkCode)
}
