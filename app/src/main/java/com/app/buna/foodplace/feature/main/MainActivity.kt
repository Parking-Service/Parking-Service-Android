package com.app.buna.foodplace.feature.main

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.app.buna.foodplace.R
import com.app.buna.foodplace.databinding.ActivityMainBinding
import com.app.buna.foodplace.feature.common.base.BaseActivity
import com.app.buna.foodplace.feature.main.adapter.CustomMarkerAdapter
import com.app.buna.foodplace.feature.main.type.LocationFabStatus
import com.app.buna.foodplace.util.MarkerManager
import com.app.buna.foodplace.util.PermissionHelper
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import net.daum.android.map.MapViewEventListener
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    override val layoutResId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    lateinit var mapView: MapView

    override fun onDestroy() {
        super.onDestroy()
        binding.mainContainer.root.removeAllViews()
    }

    override fun initActivity() {
        binding.viewModel = viewModel
        setMapView() // 카카오 맵뷰를 세팅한다.
        initView() // 뷰 초기화
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
                mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
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

    private fun setMapView() {
        val markerManager = MarkerManager()
        mapView = MapView(this).also {
            binding.mainContainer.root.addView(it)
            it.setCalloutBalloonAdapter(CustomMarkerAdapter(layoutInflater))
            it.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {
                // 현재 위치가 업데이트 되었을 때 실행되는 메소드
                // 단말의 현위치 좌표값을 통보받을 수 있다.
                override fun onCurrentLocationUpdate(mapView: MapView?, currentLocation: MapPoint?, accuracyInMeters: Float) {
                    val geoLocation: GeoCoordinate = currentLocation?.mapPointGeoCoord!!
                    markerManager.addMarker(it, markerManager.createMarker("ddd", geoLocation.latitude, geoLocation.latitude))
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

            // 주차장 데이터 리스트가 추가되거나, 변경되면 마커를 새로 찍음.
            viewModel.lotData.observe(this, { lotData ->
                markerManager.removeAllMarkers(mapView) // 현재 맵 상의 마커를 모두 지운다.
                lotData.forEach { lot ->
                    val marker = markerManager.createMarker(lot.parkName, lot.latitude.toDouble(), lot.longitude.toDouble())
                    markerManager.addMarker(mapView, marker)
                }
            })
        }


        // GPS 활성화를 위한 펄미션 체크
        PermissionHelper.checkGPSPermission(this) {
            // 권한 허용시 카카오 맵뷰를 현재 위치로 이동시킴
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
        }
    }

    private fun initView() {
        initFab()
        initDrawerLayout()
        initSearchBarContainer()
    }

    private fun initFab() {
        // 최초 접속시 현 위치로 이동하도록 하기 위함.
        if(viewModel.isMovedMyLocation.not()) { // 내 위치로 이동하지 않은 상태라면
            // 내 위치로 이동
            mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            // 라이브 데이터 UNACTIVE로 초기화
            viewModel.fabStatus.value = LocationFabStatus.UNACTIVE
        }
        // GPS 버튼을 눌렀을 때, Fab 버튼 색상 및 이미지 변경
        viewModel.fabStatus.observe(this) { fabStatus ->
            when(fabStatus) {
                LocationFabStatus.UNACTIVE -> { // 현재위치 모드, 나침반 모드 모두 사용 안함
                    if(viewModel.isMovedMyLocation){ // 내 위치로 이동한 상태라면, 정상적으로 로직 수행
                        Glide.with(this).load(R.drawable.ic_gps_double_unactive).into(binding.locationFab)
                        binding.locationFab.setColorFilter(ContextCompat.getColor(this, R.color.locationFabUnActive))
                        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
                    } else { // 최초 앱 실행시 fabStatus를 UNACTIVE로 초기화하기 때문에 실행이 됨.
                        // 따라서 flag 처리해주어야 함. 아니면 예외 발생
                        viewModel.isMovedMyLocation = true
                    }
                }
                LocationFabStatus.ACTIVE -> { // 현재위치 모드만 사용
                    Glide.with(this).load(R.drawable.ic_gps_double_unactive).into(binding.locationFab)
                    binding.locationFab.setColorFilter(ContextCompat.getColor(this, R.color.locationFabActive))
                    mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                }
                LocationFabStatus.DOUBLE_ACTIVE -> { // 현재위치 모드, 나침반 모드 모두 사용
                    Glide.with(this).load(R.drawable.ic_gps_double_active).into(binding.locationFab)
                    mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
                }
            }
        }
    }

    // 드로워 레이아웃 및 네비게이션 초기화
    private fun initDrawerLayout() {
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    // 화면 상단의 검색 뷰 초기화
    private fun initSearchBarContainer() {
        with(binding) {
            searchBarContainer.menuButton.setOnClickListener {
                // 메뉴 버튼을 누르면 DrawerLayout 열림
                binding.drawerLayout.open()
            }
        }
    }

    override fun onBackPressed() {
        // 뒤로가기를 눌렀을 때, Drawer Layout이 열려 있으면 닫는다.
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.close()
        }else {
            // 뒤로가기를 처음 눌렀거나, 누른지 2초 이상 경과했을 때 토스트 출력
            if(System.currentTimeMillis() - viewModel.backPressTime > viewModel.backPressInterval) {
                viewModel.backPressTime = System.currentTimeMillis() // 뒤로가기 버튼 누른 시간 갱신
                showToast(getString(R.string.backpress_alert)) // 뒤로가기 경고 출력
            }else {
                super.onBackPressed()
            }
        }
    }

    // 드로워 레이아웃의 네비게이션 메뉴 아이템을 선택했을 때의 리스너
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

        }

        return false
    }

}