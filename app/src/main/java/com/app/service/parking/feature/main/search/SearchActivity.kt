package com.app.service.parking.feature.main.search

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivitySearchBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.model.type.SearchMode
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {
    override val layoutResId: Int
        get() = R.layout.activity_search
    override val viewModel: SearchViewModel by viewModel()
    private lateinit var textWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataAndSet()

    }

    override fun initActivity() {
        initView() // 뷰 초기화
        binding.viewModel = viewModel // 뷰모델 초기화
    }

    private fun getDataAndSet() {
        viewModel.setSearchQuery(intent.getStringExtra("keyword")) // 음성 검색을 사용했을 경우 키워드로 검색하기 위한 변수
    }

    private fun initView() {
        binding.searchBarContainer.backButton.setOnClickListener {
            finish()
        }

        // 주차장 검색 EditText에 검색시 감지할 TextWatcher
        textWatcher = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            // 입력은 마친 후
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = binding.searchBarContainer.searchBarEditText.text.toString()
                viewModel.setSearchQuery(query) // 검색 쿼리 설정

                // 검색 쿼리가 없는 경우
                with(binding){
                    if(query.isEmpty()) {
                        // 리사이클러뷰 가리고, No Result 화면을 띄운다.
                        noResultContainer.visibility = View.VISIBLE
                        searchRecyclerView.visibility = View.GONE
                        searchBarContainer.cancelButton.visibility = View.INVISIBLE
                    } else { // 검색 쿼리가 있는 경우
                        // No Result 화면 가리고, 리사이클러뷰를 띄운다.
                        noResultContainer.visibility = View.GONE
                        searchRecyclerView.visibility = View.VISIBLE
                        searchBarContainer.cancelButton.visibility = View.VISIBLE
                    }
                }

            }
            override fun afterTextChanged(p0: Editable?) {
                // 텍스트 내용이 변경되면 리스너 재등록 (무한루프 방지하기 위해서)
                with(binding.searchBarContainer.searchBarEditText){
                    removeTextChangedListener(textWatcher)
                    addTextChangedListener(textWatcher)
                }
            }
        }
        with(binding.searchBarContainer.searchBarEditText){
            addTextChangedListener(textWatcher)
            requestFocus()

            // 액티비티를 켜면 자동으로 키보드를 올리기
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }


        // 검색 값을 받으면 리사이클러뷰 갱신
        viewModel.searchResult.observe(this) { searchResults ->

        }

        // 검색 모드를 바꿀 때마다 뷰 설정 (eg. 텍스트 -> 번호)
        viewModel.searchMode.observe(this) { mode ->
            if(mode == SearchMode.TEXT) { // 주차장명, 주소로 검색하는 경우
                with(binding.searchBarContainer) {
                    hashIcon.visibility = View.GONE
                    with(searchBarEditText) {
                        hint = getString(R.string.search_bar_address_hint)
                        setHintTextColor(ContextCompat.getColor(this@SearchActivity, R.color.searchBarHintColor))
                        setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.searchBarItemColor))
                        inputType = InputType.TYPE_CLASS_TEXT
                        cancelButton.setOnClickListener { setText("") }
                    }
                    with(searchTypeChangeButton){
                        layoutParams.height = resources.getDimension(R.dimen.search_bar_icon_hash_button_size).roundToInt()
                        layoutParams.width = resources.getDimension(R.dimen.search_bar_icon_hash_button_size).roundToInt()
                        requestLayout()
                    }
                    Glide.with(this@SearchActivity).load(R.drawable.ic_round_hash).into(searchTypeChangeButton)
                }
            }else { // 주차장 번호로 검색하는 경우
                with(binding.searchBarContainer) {
                    hashIcon.visibility = View.VISIBLE
                    with(searchBarEditText){ // 검색 Edit Text
                        hint = getString(R.string.search_bar_number_hint)
                        setHintTextColor(ContextCompat.getColor(this@SearchActivity, R.color.colorPrimary))
                        setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.colorPrimary))
                        inputType = InputType.TYPE_CLASS_NUMBER
                        cancelButton.setOnClickListener { setText("") }
                    }
                    with(searchTypeChangeButton){ // 검색타입 버튼
                        layoutParams.height = resources.getDimension(R.dimen.search_bar_icon_glasses_button_size).roundToInt()
                        layoutParams.width = resources.getDimension(R.dimen.search_bar_icon_glasses_button_size).roundToInt()
                        requestLayout()
                    }
                    Glide.with(this@SearchActivity).load(R.drawable.ic_search).into(searchTypeChangeButton)
                }
            }
        }
    }

}