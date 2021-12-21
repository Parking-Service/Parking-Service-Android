package com.app.service.parking.feature.main.favorite

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityFavoriteBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.feature.main.adapter.FavoriteRVAdapter
import com.app.service.parking.feature.main.adapter.WrapContentLinearLayoutManager
import com.app.service.parking.feature.main.review.ReviewActivity
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.repository.FavoriteRepository

class FavoriteActivity : BaseActivity<ActivityFavoriteBinding, FavoriteViewModel>() {
    override val layoutResId: Int
        get() = R.layout.activity_favorite
    override val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(
            this,
            FavoriteViewModel.Factory(FavoriteRepository(AppDB.getDatabase(this)))
        )[FavoriteViewModel::class.java]
    }
    private var rvAdapter: FavoriteRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initActivity() {
        setBindingData()
        initView()
    }

    private fun setBindingData() {
        binding.viewModel = viewModel
    }

    private fun initView() {
        rvAdapter = FavoriteRVAdapter(object : RecyclerItemClickListener {
            // 리사이클러뷰 아이템을 클릭했을 때 호출되는 리스너
            override fun onClick(position: Int, resId: Int?) {
                when (resId) {
                    R.id.delete_button -> { // 검색 결과 삭제 버튼을 클릭했을 때
                        viewModel.deleteItem(position)
                    }
                    else -> { // resId가 지정되어 있지 않은 경우 레이아웃 전체를 클릭한 것으로 간주
                        startActivity(
                            Intent(this@FavoriteActivity, ReviewActivity::class.java).putExtra(
                                "model",
                                viewModel.searchResult.value?.get(position)
                            )
                        )
                    }
                }
            }
        })

        with(binding) {
            setSupportActionBar(toolbar)
            toolbar.title = getString(R.string.favorite_toolbar_title)

            with(binding.favoriteRecyclerView) {
                setHasFixedSize(true)
                adapter = rvAdapter
                layoutManager = WrapContentLinearLayoutManager(this@FavoriteActivity)
            }
        }
    }

}