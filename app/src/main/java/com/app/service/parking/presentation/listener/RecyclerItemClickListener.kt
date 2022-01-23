package com.app.service.parking.presentation.listener

interface RecyclerItemClickListener {
    // 어떤 버튼을 클릭했는지 구분해야하는 경우엔 resId를 지정한다.
    fun onClick(position: Int, resId: Int? = null)
}