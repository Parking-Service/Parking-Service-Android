package com.app.service.parking.presentation.view.proposal

import android.app.AlertDialog
import android.view.MenuItem
import com.app.service.parking.R
import com.app.service.parking.databinding.ActivityProposalBinding
import com.app.service.parking.presentation.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProposalActivity : BaseActivity<ActivityProposalBinding, ProposalViewModel>() {
    override val layoutResId: Int = R.layout.activity_proposal
    override val viewModel: ProposalViewModel by viewModel()

    override fun initActivity() {
        with(binding) {
            setSupportActionBar(toolbar) // 해당 액티비티의 툴바 지정
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 표시
            vm = viewModel // 뷰모델 지정
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭시 정말로 종료할 것인지 묻는 다이얼로그 표시
                showBackPressDialog()
            }
        }
        return true
    }

    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭시 정말로 종료할 것인지 묻는 다이얼로그 표시
        showBackPressDialog()
    }

    // 뒤로가기 기능 사용시 보여줄 다이얼로그
    private fun showBackPressDialog() {
        val backPressDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.proposal_alert_dialog_title))
            .setMessage(getString(R.string.proposal_alert_dialog_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                finish() // 작성 취소를 확인할 경우 액티비티 종료
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                // 다이얼로그 취소 버튼 클릭시, 다이얼로그만 dismiss()
                dialog.dismiss()
            }

        // 정말 취소하고 뒤로 갈 것인지, 다이얼로그를 보여준다.
        backPressDialog.show()
    }
}