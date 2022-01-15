package com.app.service.parking.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.app.service.parking.R
import com.app.service.parking.custom.dialog.RecordBottomSheetDialog
import com.app.service.parking.presentation.review.main.ReviewBottomSheetDialog
import com.app.service.parking.databinding.ActivityMainBinding
import com.app.service.parking.presentation.base.BaseActivity
import com.app.service.parking.listener.POIItemClickListener
import com.app.service.parking.adapter.marker.CustomMarkerAdapter
import com.app.service.parking.presentation.favorite.FavoriteActivity
import com.app.service.parking.presentation.search.SearchActivity
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.preference.ParkingPreference
import com.app.service.parking.model.type.LocationFabStatus
import com.app.service.parking.util.MarkerManager
import com.app.service.parking.util.PermissionHelper
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.mapPointWithGeoCoord
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener, MapView.MapViewEventListener,
    POIItemClickListener {

    override val layoutResId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    var recordDialog: RecordBottomSheetDialog? = null
    var mRecognizer: SpeechRecognizer? = null
    var mapViewContainer: RelativeLayout? = null
    private lateinit var mapView: MapView

    // 지도를 드래그하면 새로운 위도, 경도를 가져옴 ->
    // 해당 좌표를 바탕으로 서버에 주차장 데이터 요청 ->
    // 주차장 데이터를 가져오면 MutableLiveData를 업데이트 해줌 ->
    // 데이터의 변화에 따라 새로운 마커를 등록할 수 있도록 하기 위한 Observer
    private val lotObserver = Observer<ArrayList<Lot>?> { lotData ->
        val markerManager = MarkerManager()

        markerManager.removeAllMarkers(mapView) // 현재 맵 상의 마커를 모두 지운다.
        lotData?.forEach { lot ->
            // 새로운 마커를 생성하고 맵상에 추가한다.
            var fee = if (lot.basicFee == "0") { // 기본 비용이 없으면(default값 0원) 하루당 비용 보여주기
                lot.feePerDay
            } else { // 기본 비용이 있으면 기본 비용으로 보여주기
                lot.basicFee
            }
            val marker = markerManager.createMarker(
                lot.hashCode(),
                lot.feeType,
                fee,
                lot.latitude.toDouble(),
                lot.longitude.toDouble()
            )
            markerManager.addMarker(mapView, marker)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mapView = MapView(this).also {
                mapViewContainer = RelativeLayout(this)
                mapViewContainer?.layoutParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                binding.mainContainer.mainContentContainer.addView(mapViewContainer)
                mapViewContainer?.addView(it)

                it.setCalloutBalloonAdapter(CustomMarkerAdapter(this, layoutInflater, this))
                it.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {
                    // 현재 위치가 업데이트 되었을 때 실행되는 메소드
                    // 단말의 현위치 좌표값을 통보받을 수 있다.
                    override fun onCurrentLocationUpdate(
                        mapView: MapView?,
                        currentLocation: MapPoint?,
                        accuracyInMeters: Float
                    ) {
                        // val geoLocation: GeoCoordinate = currentLocation?.mapPointGeoCoord!!

                    }

                    // 단말의 방향(Heading) 각도값을 통보받을 수 있다.
                    override fun onCurrentLocationDeviceHeadingUpdate(
                        mapView: MapView?,
                        accuracyInMeters: Float
                    ) {

                    }

                    // 현위치 갱신 작업에 실패한 경우 호출된다.
                    override fun onCurrentLocationUpdateFailed(mapView: MapView?) {
                        Timber.d("onCurrentLocationUpdate failed")
                    }

                    // 현위치 트랙킹 기능이 사용자에 의해 취소된 경우 호출된다.
                    override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {
                        Timber.d("onCurrentLocationUpdate canceled")
                    }
                })

                // 맵 뷰가 초기화 됐을때 리스너
                it.setMapViewEventListener(this)

                // SharedPreference에서 최근에 있던 위치의 위도, 경도를 가져옴
                val latitude =
                    ParkingPreference.getString(getString(R.string.latitude), "0.0")?.toDouble()
                val longitude =
                    ParkingPreference.getString(getString(R.string.longitude), "0.0")?.toDouble()

                if (latitude != 0.0 || longitude != 0.0) {
                    // 위도, 경도 지정
                    viewModel.setLocation(Pair(latitude!!, longitude!!))
                }

                // 주차장 데이터 리스트가 추가되거나, 변경되면 마커를 새로 찍음.
                viewModel.lotData.observe(this, lotObserver)
            }
        } catch (re: RuntimeException) {
            Timber.tag("에러발생").d(re.toString())
            Timber.e(re.toString())
        }
    }


    override fun initActivity() {
        binding.viewModel = viewModel
        setMapView() // 카카오 맵 초기화
        initView() // 뷰 초기화
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHelper.PERMISSION_LOCATION_REQUEST_CODE && grantResults.size == PermissionHelper.REQUIRED_PERMISSION_LOCATION.size) {
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
                moveToMyLocation()
            } else {
                // 사용자가 직접 권한을 거부했다면
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        PermissionHelper.REQUIRED_PERMISSION_LOCATION[0]
                    )
                ) {
                    showToast(getString(R.string.permission_location_denied_1)) // 위치 권한이 거부되었다는 토스트 출력
                    finish() // 앱 종료
                } else { // 사용자가 직접 권한을 거부한게 아닌, 수동적으로 거부된 상황이라면
                    showToast(getString(R.string.permission_location_denied_2))
                }
            }
        } else if (requestCode == PermissionHelper.PERMISSION_RECORD_REQUEST_CODE && grantResults.size == PermissionHelper.REQUIRED_PERMISSION_RECORD.size) {
            var isGranted = true

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                    break
                }
            }
            if (isGranted) { // 사용자가 권한을 허용했다면
                // 음성 지원 다이얼로그 보여주기
                openVoiceDialog()
            } else {
                // 사용자가 직접 권한을 거부했다면
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        PermissionHelper.REQUIRED_PERMISSION_LOCATION[0]
                    )
                ) {
                    showToast(getString(R.string.permission_record_denied))
                }
            }

        }
    }

    private fun setMapView() {

        mapView = MapView(this).also {

            mapViewContainer = RelativeLayout(this)
            mapViewContainer?.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            binding.mainContainer.mainContentContainer.addView(mapViewContainer)
            mapViewContainer?.addView(it)

            it.setCalloutBalloonAdapter(CustomMarkerAdapter(this, layoutInflater, this))
            it.setCurrentLocationEventListener(object : MapView.CurrentLocationEventListener {
                // 현재 위치가 업데이트 되었을 때 실행되는 메소드
                // 단말의 현위치 좌표값을 통보받을 수 있다.
                override fun onCurrentLocationUpdate(
                    mapView: MapView?,
                    currentLocation: MapPoint?,
                    accuracyInMeters: Float
                ) {
                    // val geoLocation: GeoCoordinate = currentLocation?.mapPointGeoCoord!!

                }

                // 단말의 방향(Heading) 각도값을 통보받을 수 있다.
                override fun onCurrentLocationDeviceHeadingUpdate(
                    mapView: MapView?,
                    accuracyInMeters: Float
                ) {

                }

                // 현위치 갱신 작업에 실패한 경우 호출된다.
                override fun onCurrentLocationUpdateFailed(mapView: MapView?) {
                    Timber.d("onCurrentLocationUpdate failed")
                }

                // 현위치 트랙킹 기능이 사용자에 의해 취소된 경우 호출된다.
                override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {
                    Timber.d("onCurrentLocationUpdate canceled")
                }
            })

            // 맵 뷰가 초기화 됐을때 리스너
            it.setMapViewEventListener(this)

            // SharedPreference에서 최근에 있던 위치의 위도, 경도를 가져옴
            val latitude =
                ParkingPreference.getString(getString(R.string.latitude), "0.0")?.toDouble()
            val longitude =
                ParkingPreference.getString(getString(R.string.longitude), "0.0")?.toDouble()

            if (latitude != 0.0 || longitude != 0.0) {
                // 위도, 경도 지정
                viewModel.setLocation(Pair(latitude!!, longitude!!))
                it.setMapCenterPoint(mapPointWithGeoCoord(latitude, longitude), false)
            }

            // 주차장 데이터 리스트가 추가되거나, 변경되면 마커를 새로 찍음.
            viewModel.lotData.observe(this, lotObserver)
        }

        // GPS 활성화를 위한 펄미션 체크
        PermissionHelper.checkGPSPermission(this) { // callback
            // 위치 권한 허용시 카카오 맵뷰를 현재 위치로 이동시킴
            moveToMyLocation()
        }
    }

    private fun initView() {
        initFab()
        initDrawerLayout()
        initSearchBarContainer()
    }

    private fun initFab() {
        if (PermissionHelper.hasGPSPermission(this)) { // GPS 권한을 허용한 사용자면
            moveToMyLocation() // 내 위치로 이동
        }

        // GPS 버튼을 눌렀을   때, Fab 버튼 색상 및 이미지 변경
        // Kakao Map 모드 변경
        viewModel.fabStatus.observe(this) { fabStatus ->
            when (fabStatus) {
                LocationFabStatus.UNACTIVE -> { // 현재위치 모드, 나침반 모드 모두 사용 안함
                    if (viewModel.isMovedMyLocation) { // 내 위치로 이동한 상태라면, 정상적으로 로직 수행
                        Glide.with(this).load(R.drawable.ic_gps_double_unactive)
                            .into(binding.locationFab)
                        binding.locationFab.setColorFilter(
                            ContextCompat.getColor(
                                this,
                                R.color.locationFabUnActive
                            )
                        )
                        mapView.currentLocationTrackingMode =
                            MapView.CurrentLocationTrackingMode.TrackingModeOff
                    } else { // 최초 앱 실행시 fabStatus를 UNACTIVE로 초기화하기 때문에 실행이 됨.
                        // 따라서 flag 처리해주어야 함. 아니면 예외 발생
                        viewModel.isMovedMyLocation = true
                    }
                }
                LocationFabStatus.ACTIVE -> { // 현재위치 모드만 사용
                    Glide.with(this).load(R.drawable.ic_gps_double_unactive)
                        .into(binding.locationFab)
                    binding.locationFab.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.locationFabActive
                        )
                    )
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
                }
                LocationFabStatus.DOUBLE_ACTIVE -> { // 현재위치 모드, 나침반 모드 모두 사용
                    Glide.with(this).load(R.drawable.ic_gps_double_active).into(binding.locationFab)
                    mapView.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading
                    binding.locationFab.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.locationFabActive
                        )
                    )
                }
            }
        }
    }

    private fun moveToMyLocation() {
        // 최초 접속시 현 위치로 이동하도록 하기 위함.
        if (viewModel.isMovedMyLocation.not()) { // 내 위치로 이동하지 않은 상태라면
            // 내 위치로 이동
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
            // 라이브 데이터 UNACTIVE로 초기화
            viewModel.fabStatus.value = LocationFabStatus.UNACTIVE
        }
    }

    // 드로워 레이아웃 및 내비게이션 초기화
    private fun initDrawerLayout() {
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    // 화면 상단의 검색 뷰 초기화
    private fun initSearchBarContainer() {
        recordDialog = RecordBottomSheetDialog(this)
        with(binding) {
            searchBarContainer.menuButton.setOnClickListener {
                // 메뉴 버튼을 누르면 DrawerLayout 열림
                binding.drawerLayout.open()
            }
            searchBarContainer.voiceButton.setOnClickListener {
                PermissionHelper.checkRecordPermission(this@MainActivity) { // 권한 승인된 상태이면
                    openVoiceDialog()
                }
            }
            searchBarContainer.searchBar.setOnClickListener {
                MarkerManager().removeAllMarkers(mapView)
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // 다음 맵 특성 상, 하나의 지도밖에 포함하지 못하므로 onStop() 호출시 맵뷰를 제거해준다.
        mapViewContainer?.removeView(mapView)
    }

    // 음성으로 검색하기 다이얼로그 보여주기
    private fun openVoiceDialog() {
        recordDialog?.let {
            if (it.isShown.not()) {
                it.show() // 음성인식 다이얼로그 show()
                mRecognizer =
                    SpeechRecognizer.createSpeechRecognizer(this) // 새로운 SpeechRecognizer를 만드는 팩토리 메서드
                mRecognizer?.setRecognitionListener(getRecognitionListener())
                mRecognizer?.startListening(
                    // 여분의 키
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        packageName
                    ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 설정
                )
            }
        }
    }

    private fun dismissVoiceDialog() {
        mRecognizer?.stopListening() // 음성인식 종료
        mRecognizer?.destroy()
        if (recordDialog?.isShown == true) {
            recordDialog?.dismiss()
        }
    }

    // 음성인식 리스너
    private fun getRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {

            // 말하기 시작할 준비가되면 호출
            override fun onReadyForSpeech(params: Bundle?) {}

            // 말하기 시작했을 때 호출
            override fun onBeginningOfSpeech() {}

            // 입력받는 소리의 크기를 알려줌
            override fun onRmsChanged(dB: Float) {}

            // 말을 시작하고 인식이 된 단어를 buffer에 담음
            override fun onBufferReceived(p0: ByteArray?) {}

            // 말하기가 끝났을 때
            override fun onEndOfSpeech() {}

            // 에러 발생
            override fun onError(error: Int) {
                /*val message: String = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> getString(R.string.error_audio) // 말이 없을 때
                    SpeechRecognizer.ERROR_CLIENT -> getString(R.string.error_client) // 말이 너무 길때
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> getString(R.string.error_permission)
                    SpeechRecognizer.ERROR_NETWORK -> getString(R.string.error_network)
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> getString(R.string.error_network_timeout)
                    SpeechRecognizer.ERROR_NO_MATCH -> getString(R.string.error_no_match)
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> getString(R.string.error_busy)
                    SpeechRecognizer.ERROR_SERVER -> getString(R.string.error_server)
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> getString(R.string.error_speech_timeout)
                    else -> getString(R.string.error_unknown)
                }*/

                mRecognizer?.cancel()
                mRecognizer?.startListening(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        packageName
                    ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
                )
            }

            // 인식 결과가 준비되면 호출
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            override fun onResults(results: Bundle?) {
                val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                MarkerManager().removeAllMarkers(mapView)
                startActivity(
                    Intent(
                        this@MainActivity,
                        SearchActivity::class.java
                    ).putExtra("query", matches?.get(0))
                )
                dismissVoiceDialog()
            }

            // 부분 인식 결과를 사용할 수 있을 때 호출
            override fun onPartialResults(p0: Bundle?) {}

            // 향후 이벤트를 추가하기 위해 예약
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }

    override fun onBackPressed() {
        // 뒤로가기를 눌렀을 때, Drawer Layout이 열려 있으면 닫는다.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.close()
        } else if (recordDialog?.isShown!!) {
            dismissVoiceDialog()
        } else {
            // 뒤로가기를 처음 눌렀거나, 누른지 2초 이상 경과했을 때 토스트 출력
            if (System.currentTimeMillis() - viewModel.backPressTime > viewModel.backPressInterval) {
                viewModel.backPressTime = System.currentTimeMillis() // 뒤로가기 버튼 누른 시간 갱신
                showToast(getString(R.string.backpress_alert)) // 뒤로가기 경고 출력
            } else {
                super.onBackPressed()
            }
        }
    }

    // Android 3.2 이상에서
    // manifest -> activity의 android:configChanges="orientation|screenSize" 지정해주어야함.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {//세로 전환시
            binding.askContainer.visibility = View.VISIBLE
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { //가로전환시
            binding.askContainer.visibility = View.GONE
        }
    }

    // 드로워 레이아웃의 내비게이션 메뉴 아이템을 선택했을 때의 리스너
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return false
    }

    override fun onMapViewInitialized(p0: MapView?) {}

    // 맵 중심점이 바뀔 때마다 호출
    override fun onMapViewCenterPointMoved(p0: MapView?, mapPoint: MapPoint?) {
        // 지도 드래그를 마칠 때마다, 주차장 데이터를 가져와서 맵 상에 마커를 띄우도록 한다.
        val location = mapPoint?.mapPointGeoCoord // 좌표 객체
        val latitude: Double = location?.latitude ?: 0.0 // 위도
        val longitude: Double = location?.longitude ?: 0.0 // 경도

        // Preference에 가장 최근 위도, 경도 저장
        ParkingPreference.putValue(getString(R.string.latitude), latitude.toString())
        ParkingPreference.putValue(getString(R.string.longitude), longitude.toString())

        // 현재 좌표 지정(마커를 찍기 위함)
        viewModel.setLocation(Pair(latitude, longitude))
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        viewModel.fabStatus.value = LocationFabStatus.UNACTIVE
    }

    override fun onMapViewDragEnded(mapView: MapView?, mapPoint: MapPoint?) {}

    override fun onMapViewMoveFinished(mapView: MapView?, mapPoint: MapPoint?) {}

    /* POIItemClickListener */
    override fun onClick(marker: MapPOIItem?) {
        var findModel: Lot? = null
        viewModel.lotData.value?.forEach { lot ->
            //Log.d("태그", "${lot.hashCode()} vs ${marker?.tag}")
            if (marker?.tag == lot.hashCode()) {
                findModel = lot
                return@forEach
            }
        }

        findModel?.let { lotModel ->
            val reviewDialog = ReviewBottomSheetDialog(lotModel)
            reviewDialog.show(supportFragmentManager, "ReviewDialog")
        }
    }
}