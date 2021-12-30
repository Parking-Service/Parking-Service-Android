package com.app.service.parking.model.type

enum class RateStatus {
    GOOD {
        override fun getChangedStatus(): RateStatus {
            return GOOD
        }
    }, NORMAL {
        override fun getChangedStatus(): RateStatus {
            return NORMAL
        }
    }, BAD {
        override fun getChangedStatus(): RateStatus {
            return BAD
        }
    };

    abstract fun getChangedStatus(): RateStatus
}