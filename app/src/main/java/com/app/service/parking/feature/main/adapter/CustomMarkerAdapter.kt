package com.app.service.parking.feature.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.service.parking.R
import com.app.service.parking.databinding.ItemFreeMarkerBinding
import com.app.service.parking.databinding.ItemPayMarkerBinding
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import java.text.DecimalFormat

class CustomMarkerAdapter(val context: Context, inflater: LayoutInflater) : CalloutBalloonAdapter {
    // 무료 주차장 마커 바인딩
    val freeBinding = DataBindingUtil.inflate<ItemFreeMarkerBinding>(
        inflater,
        R.layout.item_free_marker,
        null,
        false
    )
    // 유료 주차장 마커 바인딩
    val payBinding = DataBindingUtil.inflate<ItemPayMarkerBinding>(
        inflater,
        R.layout.item_pay_marker,
        null,
        false
    )

    // 마커 클릭시 나오는 말풍선의 뷰 구성
    override fun getCalloutBalloon(marker: MapPOIItem?): View? {
        // itemName = 가격
        val fee = marker?.itemName

        return if (fee.equals("0") || fee.isNullOrEmpty()) { // 가격
            freeBinding.root
        } else {
            payBinding.feeTextView.text = DecimalFormat("#,###").format(Integer.parseInt(marker?.itemName))
            payBinding.root
        }
    }

    // 말풍선 클릭시
    override fun getPressedCalloutBalloon(marker: MapPOIItem?): View? {
        /*// 리뷰 액티비티 시작할 때 맵뷰 제거 (맵뷰 2개 이상 동시에 불가)
        val intent = Intent(context, ReviewActivity::class.java)
        context.startActivity(intent)*/
        // itemName = 가격
        val fee = marker?.itemName

        return if (fee.equals("0") || fee.isNullOrEmpty()) { // 가격
            freeBinding.root
        } else {
            payBinding.root
        }
    }
}