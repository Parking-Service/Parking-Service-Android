package com.app.service.parking.util

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.app.service.parking.R
import com.app.service.parking.global.App
import com.google.android.material.snackbar.Snackbar

object GPSStatus {
    val mLocMan = App.context?.getSystemService(Context.LOCATION_SERVICE) as (LocationManager)

    // GPS가 켜져있는 확인해주는 Method
    fun checkGPS(view: View): Boolean {
        // GPS가 꺼져있으면 스낵바 보여주기
        if(mLocMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true
        } else {
            showGPSCheckDialog(view, App.context!!.applicationContext)
            return false
        }
    }

    // GPS가 꺼져있을 때, 켜야한다는 스낵바 보여주기
    private fun showGPSCheckDialog(view: View, context: Context) {
        val snackbar = Snackbar.make(
            view,
            context.getString(R.string.snack_bar_content_gps_disconnected),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(context.getString(R.string.ok)) {
            // GPS 설정 화면으로 이동
            val gpsOptionsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            view.context.startActivity(gpsOptionsIntent)
        }
            .setTextColor(ContextCompat.getColor(context, R.color.snackbarTextColor))
            .setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimaryVariant))
            .setBackgroundTint(ContextCompat.getColor(context, R.color.snackbarBackground))
            .show()
    }
}