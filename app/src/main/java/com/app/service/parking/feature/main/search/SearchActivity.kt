package com.app.service.parking.feature.main.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.service.parking.R
import com.app.service.parking.custom.RecordBottomSheetDialog
import com.app.service.parking.databinding.ActivitySearchBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.feature.main.adapter.SearchRVAdapter
import com.app.service.parking.feature.main.adapter.WrapContentLinearLayoutManager
import com.app.service.parking.feature.main.review.ReviewActivity
import com.app.service.parking.model.type.SearchMode
import com.app.service.parking.util.PermissionHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {
    override val layoutResId: Int
        get() = R.layout.activity_search
    override val viewModel: SearchViewModel by viewModel()
    private lateinit var textWatcher: TextWatcher
    private var rvAdapter: SearchRVAdapter? = null
    var recordDialog: RecordBottomSheetDialog? = null
    var mRecognizer: SpeechRecognizer? = null
    private val delayTime = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 음성인식으로 SearchActivity에 들어온 경우 음성 결과값으로 검색
        CoroutineScope(Dispatchers.Default).launch {
            setQueryIntent() // 바로 실행하면 쿼리 적용이 안됨.
        }
    }

    override fun initActivity() {
        initView() // 뷰 초기화
        binding.viewModel = viewModel // 뷰모델 초기화
        recordDialog = RecordBottomSheetDialog(this)
    }

    private suspend fun setQueryIntent() {
        val query: String? = intent.getStringExtra("query") // 음성 검색을 사용했을 경우 키워드로 검색하기 위한 변수

        query?.let { query ->
            withContext(Dispatchers.Main) {
                // 리사이클러뷰를 보여주고, No Result 화면을 가린다.
                binding.noResultContainer.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.VISIBLE
            }

            delay(delayTime) // delayTime 후에 쿼리 지정
            if (query.length > 1) {
                viewModel.setSearchQuery(query) // ViewModel 쿼리 지정
            }
        }
    }

    private fun initView() {
        // 리사이클러뷰 설정
        rvAdapter = SearchRVAdapter(object : RecyclerItemClickListener {
            // 리사이클러뷰 아이템을 클릭했을 때 호출되는 리스너
            override fun onClick(position: Int, resId: Int?) {
                when (resId) {
                    R.id.delete_button -> { // 검색 결과 삭제 버튼을 클릭했을 때
                        viewModel.deleteItem(position)
                    }
                    else -> { // resId가 지정되어 있지 않은 경우 레이아웃 전체를 클릭한 것으로 간주
                        startActivity(
                            Intent(this@SearchActivity, ReviewActivity::class.java).putExtra(
                                "model",
                                viewModel.searchResult.value?.get(position)
                            )
                        )
                    }
                }
            }
        })
        with(binding.searchRecyclerView) {
            setHasFixedSize(true)
            adapter = rvAdapter
            layoutManager = WrapContentLinearLayoutManager(this@SearchActivity)
        }

        binding.searchBarContainer.backButton.setOnClickListener {
            finish()
        }

        // 주차장 검색 EditText에 검색시 감지할 TextWatcher
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            // 입력은 마친 후
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = binding.searchBarContainer.searchBarEditText.text.toString()
                viewModel.setSearchQuery(query) // 검색 쿼리 설정

                // 검색 쿼리가 없는 경우
                with(binding) {
                    if (query.isEmpty()) {
                        // 리사이클러뷰 가리고, No Result 화면을 띄운다.
                        noResultContainer.visibility = View.VISIBLE
                        searchRecyclerView.visibility = View.GONE
                        searchBarContainer.cancelButton.visibility = View.INVISIBLE
                        searchProgressBar.visibility = View.GONE // 프로그레스바 가리기
                    } else { // 검색 쿼리가 있는 경우
                        // No Result 화면 가리고, 리사이클러뷰를 띄운다.
                        noResultContainer.visibility = View.GONE
                        searchRecyclerView.visibility = View.VISIBLE
                        searchBarContainer.cancelButton.visibility = View.VISIBLE
                        if (query.length > 1) {
                            searchProgressBar.visibility = View.VISIBLE // 프로그레스바 보여주기
                        } else {
                            searchProgressBar.visibility = View.GONE // 프로그레스바 가리기
                        }
                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                // 텍스트 내용이 변경되면 리스너 재등록 (무한루프 방지하기 위해서)
                with(binding.searchBarContainer.searchBarEditText) {
                    removeTextChangedListener(textWatcher)
                    addTextChangedListener(textWatcher)
                }
            }
        }
        with(binding.searchBarContainer.searchBarEditText) {
            addTextChangedListener(textWatcher)
            requestFocus()


            if ((intent.getStringExtra("query")?.length ?: 0) < 2) {
                // 액티비티를 켜면 자동으로 키보드를 올리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                this.postDelayed({
                    imm.showSoftInput(this, 0)
                }, 500)
            }
        }

        // 검색 값을 받으면 리사이클러뷰 갱신
        viewModel.searchResult.observe(this) { searchResult ->
            // 쿼리가 한번 이상 설정된 상태이면 (음성인식으로 들어왔을 때 StateFlow 초기화로 인해 무조건 한번 실행되는 상황을 방지)
            if (viewModel.isSetQuery()) {
                with(binding) {
                    // 검색 쿼리가 없는 경우
                    if (searchResult.isEmpty()) {
                        // 리사이클러뷰 가리고, No Result 화면을 띄운다.
                        noResultContainer.visibility = View.VISIBLE
                        searchRecyclerView.visibility = View.GONE
                    } else { // 검색 쿼리가 있는 경우
                        // No Result 화면 가리고, 리사이클러뷰를 띄운다.
                        noResultContainer.visibility = View.GONE
                        searchRecyclerView.visibility = View.VISIBLE
                    }
                }

                CoroutineScope(Dispatchers.Main).launch {
                    rvAdapter?.updateItems(searchResult) // 리사이클러뷰 어댑터에 새로 가져온 주차장 데이터 업데이트
                    binding.searchProgressBar.visibility = View.GONE // 결과를 다 가져왔으면, 프로그레스바 가리기
                }
            }
        }

        // 검색 모드를 바꿀 때마다 뷰 설정 (eg. 텍스트 -> 번호)
        viewModel.searchMode.observe(this) { mode ->
            if (mode == SearchMode.TEXT) { // 주차장명, 주소로 검색하는 경우
                with(binding.searchBarContainer) {
                    Glide.with(this@SearchActivity).load(R.drawable.ic_round_hash)
                        .into(searchTypeChangeButton)
                    hashIcon.visibility = View.GONE
                    with(searchBarEditText) {
                        hint = getString(R.string.search_bar_address_hint)
                        setHintTextColor(
                            ContextCompat.getColor(
                                this@SearchActivity,
                                R.color.searchBarHintColor
                            )
                        )
                        setTextColor(
                            ContextCompat.getColor(
                                this@SearchActivity,
                                R.color.searchBarItemColor
                            )
                        )
                        inputType = InputType.TYPE_CLASS_TEXT
                        cancelButton.setOnClickListener { setText("") }
                    }
                    with(searchTypeChangeButton) {
                        // 아이콘 사이즈 변경
                        layoutParams.height =
                            resources.getDimension(R.dimen.search_bar_icon_hash_button_size)
                                .roundToInt()
                        layoutParams.width =
                            resources.getDimension(R.dimen.search_bar_icon_hash_button_size)
                                .roundToInt()
                        requestLayout()

                    }
                }
            } else { // 주차장 번호로 검색하는 경우
                with(binding.searchBarContainer) {
                    Glide.with(this@SearchActivity).load(R.drawable.ic_search)
                        .into(searchTypeChangeButton)
                    hashIcon.visibility = View.VISIBLE
                    with(searchBarEditText) { // 검색 Edit Text
                        hint = getString(R.string.search_bar_number_hint)
                        setHintTextColor(
                            ContextCompat.getColor(
                                this@SearchActivity,
                                R.color.colorPrimary
                            )
                        )
                        setTextColor(
                            ContextCompat.getColor(
                                this@SearchActivity,
                                R.color.colorPrimary
                            )
                        )
                        inputType = InputType.TYPE_CLASS_NUMBER
                        cancelButton.setOnClickListener { setText("") }
                    }
                    with(searchTypeChangeButton) { // 검색타입 버튼
                        layoutParams.height =
                            resources.getDimension(R.dimen.search_bar_icon_glasses_button_size)
                                .roundToInt()
                        layoutParams.width =
                            resources.getDimension(R.dimen.search_bar_icon_glasses_button_size)
                                .roundToInt()
                        requestLayout()
                    }
                }
            }

            // 검색 타입 바꾸면 에딧텍스트 비우기
            binding.searchBarContainer.searchBarEditText.text.clear()
        }

        binding.searchBarContainer.voiceButton.setOnClickListener {
            PermissionHelper.checkRecordPermission(this@SearchActivity) { // 권한 승인된 상태라면
                openVoiceDialog() // 음성인식 다이얼로그 오픈
            }
        }
    }


    // 음성으로 검색하기 다이얼로그 보여주기
    private fun openVoiceDialog() {
        recordDialog?.let {
            if (it.isShown.not()) {
                // 키보드 닫기
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    binding.searchBarContainer.searchBarEditText.windowToken,
                    0
                )

                // 키보드 닫는 시간을 위해 0.5초 뒤에 다이얼로그 보여주기
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500L)
                    it.show()
                    if (mRecognizer == null) {
                        mRecognizer =
                            SpeechRecognizer.createSpeechRecognizer(baseContext) // 새로운 SpeechRecognizer를 만드는 팩토리 메서드
                        mRecognizer?.setRecognitionListener(getRecognitionListener())
                    }

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
                val match =
                    results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                viewModel.setSearchQuery(match)
                recordDialog?.dismiss()
                binding.searchProgressBar.visibility = View.VISIBLE // 프로그레스바 보여주기
                // No Result 화면을 가리고, 리사이클러뷰를 띄운다.
                binding.noResultContainer.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
            }

            // 부분 인식 결과를 사용할 수 있을 때 호출
            override fun onPartialResults(p0: Bundle?) {}

            // 향후 이벤트를 추가하기 위해 예약
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionHelper.PERMISSIONS_RECORD_REQUEST_CODE && grantResults.size == PermissionHelper.REQUIRED_PERMISSION_RECORD.size) {
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

    override fun onBackPressed() {
        // 뒤로가기를 눌렀을 때, Drawer Layout이 열려 있으면 닫는다.
        if (recordDialog?.isShown!!) {
            dismissVoiceDialog()
        } else {
            finish()
        }
    }

}