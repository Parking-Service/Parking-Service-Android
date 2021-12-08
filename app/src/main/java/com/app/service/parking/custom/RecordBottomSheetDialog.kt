package com.app.service.parking.custom

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.app.service.parking.R
import com.app.service.parking.databinding.LayoutRecordBottomSheetBinding
import com.arthurivanets.bottomsheets.BaseBottomSheet
import com.arthurivanets.bottomsheets.config.BaseConfig
import com.arthurivanets.bottomsheets.config.Config

class RecordBottomSheetDialog(
    hostActivity: Activity,
    config: BaseConfig = Config.Builder(hostActivity).build()
) : BaseBottomSheet(hostActivity, config) {

    lateinit var binding: LayoutRecordBottomSheetBinding

    override fun onCreateSheetContentView(context: Context): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.layout_record_bottom_sheet,
            this,
            false
        )
        // 닫기 버튼 누르면 dismiss()
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

}