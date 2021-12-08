package com.app.service.parking.model.type

enum class LocationFabStatus {
    // Fab 버튼이 아직 안눌렸을 때
    UNACTIVE {
        override fun getNextStatus(): LocationFabStatus {
            return ACTIVE
        }
    },

    // Fab 버튼을 눌렀을 때
    ACTIVE {
        override fun getNextStatus(): LocationFabStatus {
            return DOUBLE_ACTIVE
        }
    },

    // Fab 버튼이 눌리고 한 번 더 눌렸을 때
    DOUBLE_ACTIVE {
        override fun getNextStatus(): LocationFabStatus {
            return UNACTIVE
        }
    };

    // 다음 Location Fab버튼 상태를 가져오는 함수
    abstract fun getNextStatus(): LocationFabStatus
}