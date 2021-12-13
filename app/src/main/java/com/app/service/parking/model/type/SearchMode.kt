package com.app.service.parking.model.type


enum class SearchMode {
    TEXT {
        override fun toggleMode(): SearchMode {
            return NUMBER
        }
    }, NUMBER {
        override fun toggleMode(): SearchMode {
            return TEXT
        }
    };

    abstract fun toggleMode(): SearchMode
}