package com.app.buna.foodplace.feature.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.app.buna.foodplace.R
import com.app.buna.foodplace.databinding.ActivityMainBinding
import com.app.buna.foodplace.databinding.ItemMarkerBinding
import com.app.buna.foodplace.feature.common.base.BaseActivity
import com.app.buna.foodplace.feature.main.adapter.CustomMarkerAdapter
import com.app.buna.foodplace.util.PermissionHelper
import net.daum.android.map.MapViewEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mainContainer.root.removeAllViews()
    }

    override fun initActivity() {
        setMapView()
    }

    private fun setMapView() {
        mapView = MapView(this).also {
            binding.mainContainer.root.addView(it)
            it.setCalloutBalloonAdapter(CustomMarkerAdapter(layoutInflater))
            it.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {
                // 현재 위치가 업데이트 되었을 때 실행되는 메소드
                // 단말의 현위치 좌표값을 통보받을 수 있다.
                override fun onCurrentLocationUpdate(mapView: MapView?, currentLocation: MapPoint?, accuracyInMeters: Float) {
                    val geoLocation: GeoCoordinate = currentLocation?.mapPointGeoCoord!!
                }
                // 단말의 방향(Heading) 각도값을 통보받을 수 있다.
                override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView?, accuracyInMeters: Float) {}
                // 현위치 갱신 작업에 실패한 경우 호출된다.
                override fun onCurrentLocationUpdateFailed(mapView: MapView?) {}
                // 현위치 트랙킹 기능이 사용자에 의해 취소된 경우 호출된다.
                override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {}
            })

            // 맵 뷰가 초기화 됐을때 리스너
            it.mapViewEventListener = MapViewEventListener {

            }
        }

        mapView?.addPOIItem(createMarker(37.621036529541016, 126.83155822753906))

        // GPS 활성화를 위한 펄미션 체크
        PermissionHelper.checkGPSPermission(this) {
            // 권한 허용시 카카오 맵뷰를 현재 위치로 이동시킴
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
        }
    }

    private fun createMarker(latitude: Double, longitude: Double): MapPOIItem {
        val marker = MapPOIItem().apply {
            itemName = "우리집"
            mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
            markerType = MapPOIItem.MarkerType.CustomImage
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.free
            customSelectedImageResourceId =R.drawable.free
            isCustomImageAutoscale = false  // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            setCustomImageAnchor(0.5f, 1.0f) // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        }

        return marker
    }

    private fun addMarker(marker: MapPOIItem) {
        mapView.addPOIItem(marker)
    }

    private fun removeAllMarkers() {
        mapView.removeAllPOIItems()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHelper.PERMISSIONS_LOCATION_REQUEST_CODE && grantResults.size == PermissionHelper.REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var isGranted = true

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                    break
                }
            }
            if (isGranted) { // 사용자가 권한을 허용했다면
                // 카카오 맵을 현재 내 위치로 이동시킴
                mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
            } else {
                // 사용자가 직접 권한을 거부했다면
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PermissionHelper.REQUIRED_PERMISSIONS[0])) {
                    showToast(getString(R.string.permission_location_denied_1)) // 위치 권한이 거부되었다는 토스트 출력
                    finish() // 앱 종료
                } else { // 사용자가 직접 권한을 거부한게 아닌, 수동적으로 거부된 상황이라면
                    showToast(getString(R.string.permission_location_denied_2))
                }
            }
        }
    }

}