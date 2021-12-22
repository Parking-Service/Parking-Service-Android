package com.app.service.parking.feature.main.favorite

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityFavoriteBinding
import com.app.service.parking.feature.base.BaseActivity
import com.app.service.parking.feature.listener.RecyclerItemClickListener
import com.app.service.parking.feature.main.adapter.FavoriteRVAdapter
import com.app.service.parking.feature.main.review.ReviewActivity
import com.app.service.parking.model.dto.Lot
import com.app.service.parking.model.repository.local.db.AppDB
import com.app.service.parking.model.repository.local.entity.EntityFavorite
import com.app.service.parking.model.repository.local.repository.FavoriteRepository

class FavoriteActivity : BaseActivity<ActivityFavoriteBinding, FavoriteViewModel>() {
    override val layoutResId: Int
        get() = R.layout.activity_favorite
    override val viewModel: FavoriteViewModel by lazy {
        ViewModelProvider(
            this,
            FavoriteViewModel.Factory(FavoriteRepository.getInstance(AppDB.getDatabase(this))!!)
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
                        rvAdapter?.notifyItemRemoved(position)
                        rvAdapter?.notifyItemRangeChanged(position, rvAdapter?.lots?.size!!)
                    }
                    else -> { // resId가 지정되어 있지 않은 경우 레이아웃 전체를 클릭한 것으로 간주
                        startActivity(
                            Intent(this@FavoriteActivity, ReviewActivity::class.java).putExtra(
                                "model",
                                viewModel.favoriteLotList[position]
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
                layoutManager = FavoriteRVAdapter.WrapContentLinearLayoutManager(this@FavoriteActivity)
            }
        }

        viewModel.favoriteEntityList.observe(this, { entityList ->
            // Entity class를 Lot class로 변환
            rvAdapter?.updateItems(entityToLot(entityList))
        })
    }

    // 엔티티 클래스로 받아온 주차장 즐겨찾기 데이터를
    // Lot Class로 변환
    private fun entityToLot(entityList: List<EntityFavorite>): ArrayList<Lot> {
        // 기존 즐겨찾기 주차장 데이터 리스트 초기화
        viewModel.favoriteLotList.clear()
        
        // Entity 리스트 안의 요소들을 favoriteLotList로 이동
        entityList.forEach { entityFavorite ->
            with(entityFavorite) {
                viewModel.favoriteLotList.add(
                    Lot(
                        parkCode,
                        parkName,
                        "",
                        "",
                        newAddr,
                        oldAddr,
                        "",
                        "",
                        "",
                        operDay,
                        weekdayOpenTime,
                        weekdayCloseTime,
                        saturdayOpenTime,
                        saturdayCloseTime,
                        holidayOpenTime,
                        holidayCloseTime,
                        feeType,
                        basicParkTime,
                        basicFee,
                        addUnitTime,
                        addUnitFee,
                        parkTimePerDay,
                        feePerDay,
                        feePerMonth,
                        payType,
                        uniqueness,
                        "",
                        phoneNumber,
                        latitude,
                        longitude,
                        "",
                        "",
                        ""
                    )
                )
            }
        }

        return viewModel.favoriteLotList
    }

}