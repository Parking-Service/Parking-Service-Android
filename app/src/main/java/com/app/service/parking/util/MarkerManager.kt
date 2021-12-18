package com.app.service.parking.util

import com.app.service.parking.R
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MarkerManager {

    // 마커 인스턴스를 생성한다.
    fun createMarker(type: String?, fee: String?, latitude: Double, longitude: Double): MapPOIItem {
        val marker = MapPOIItem().apply {
            itemName = "$fee@$type" // 마커 이름 설정
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude) // 마커 좌표 설정
            markerType = MapPOIItem.MarkerType.CustomImage // 일반 마커 커스텀 이미지 사용 지정
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage // 선택됐을 때 마커 커스텀 이미지 사용 지정
            isCustomImageAutoscale = true  // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            setCustomImageAnchor(0.5f, 1.0f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            if(type == "무료") { // 무료 주차장일때 마커 이미지
                customImageResourceId = R.drawable.ic_free
                customSelectedImageResourceId = R.drawable.ic_free
            } else { // 유료 주차장일때 마커 이미지
                customImageResourceId = R.drawable.ic_pay
                customSelectedImageResourceId = R.drawable.ic_pay
            }
        }

        return marker
    }

    // 맵상에 마커를 추가한다.
    fun addMarker(mapView: MapView?, marker: MapPOIItem) {
        mapView?.addPOIItem(marker)
    }

    // 맵상의 마커를 모두 제거한다.
    fun removeAllMarkers(mapView: MapView?) {
        mapView?.removeAllPOIItems()
    }
}