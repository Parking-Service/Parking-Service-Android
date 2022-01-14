package com.app.service.parking.util

import android.content.Context
import com.ceylonlabs.imageviewpopup.ImagePopup

class PopupImage {
    // 리뷰를 클릭했을 때, 리뷰 이미지를 크게 보여주는 팝업창
    fun showImagePopup(context: Context, imgUrl: String?) {
        // 주차장 사진 url이 함께 존재한다면
        if (imgUrl.isNullOrBlank().not()) {
            with(ImagePopup(context)){
                initiatePopupWithGlide(imgUrl)
                viewPopup() // Load Image from Review Url
            }
        }
    }
}