package com.app.service.parking.presentation.listener

import net.daum.mf.map.api.MapPOIItem

interface POIItemClickListener {
    fun onClick(marker: MapPOIItem?)
}