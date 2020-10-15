package org.davilo.app.ui.activity.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.ActivityConfirmationBinding
import org.davilo.app.di.network.utils.ServerException
import org.davilo.app.ui.activity.main.ActivityMain
import org.davilo.app.ui.base.BaseActivity
import org.davilo.app.utils.afterTextChanged

@AndroidEntryPoint
class ActivityConfirmation : BaseActivity<ActivityConfirmationBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_confirmation

    private val viewModel: ActivityConfirmationViewModel by viewModels()

    private var countDownTimer: CountDownTimer? = null
    private var isCountDownTimerStarting: Boolean = false
    private var username: String? = null
    private var password: String? = null
    private var shouldRequestCode: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getExtras()

        initView()

        observeLiveDate()

    }

    private fun getExtras() {
        username = intent?.getStringExtra(EXTRA_USERNAME)
        password = intent?.getStringExtra(EXTRA_PASSWORD)
        shouldRequestCode = intent?.getBooleanExtra(EXTRA_SHOULD_REQUEST_CODE, false)

    }

    private fun initView() {

        binding.btnSubmit.isEnabled = false

        if (shouldRequestCode == true) {
            binding.pbResend.visibility = View.VISIBLE
            viewModel.resendCode(username)
        } else {
            startCountDownTimer()
        }

        binding.edtConfirmCode.afterTextChanged {
            binding.edtConfirmCode.error = null
            binding.btnSubmit.isEnabled = it.length == 6

        }

        binding.btnSubmit.setOnClickListener(this)
        binding.btnResend.setOnClickListener(this)
    }

    private fun observeLiveDate() {
        viewModel.errorLiveData.observe(this, Observer { error ->
            binding.pbResend.visibility = View.INVISIBLE
            binding.loading.visibility = View.INVISIBLE
            if (isCountDownTimerStarting) {
                binding.btnSubmit.isEnabled = true
            } else {
                binding.btnResend.isEnabled = true
            }
            if (error is ServerException) {
                binding.edtConfirmCode.error = error.output?.status?.status_message
            }
        })

        viewModel.resendConfirmEmailLiveData.observe(this, Observer {
            binding.btnResend.visibility = View.INVISIBLE
            binding.pbResend.visibility = View.INVISIBLE
            startCountDownTimer()

        })
        viewModel.confirmEmailLiveData.observe(this, Observer { response ->
            viewModel.saveUserInfo(response)
            finishAffinity()
            ActivityMain.navigate(this)
        })
    }

    private fun startCountDownTimer() {
        binding.txtTimer.visibility = View.VISIBLE
        initCountDownTimer(600, binding.txtTimer) {
            binding.txtTimer.visibility = View.INVISIBLE
            binding.btnResend.isEnabled = true
            binding.btnResend.visibility = View.VISIBLE
        }
    }

    private fun initCountDownTimer(timeSecond: Long, textView: TextView, onFinished: () -> Unit) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeSecond * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                try {
                    val remainTime = (millisUntilFinished / 1000)
                    viewModel.saveRemainingTimeInSharedPreferences(remainTime)
                    textView.text = String.format(
                        "%s %s",
                        "${remainTime / 60}:${if (remainTime % 60 < 10) "0" else ""}${remainTime % 60}",
                        "Time remaining"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                isCountDownTimerStarting = false
                onFinished.invoke()
            }
        }
        isCountDownTimerStarting = true
        countDownTimer?.start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_submit -> {
                binding.btnResend.isEnabled = false
                binding.loading.visibility = View.VISIBLE
                viewModel.confirm(username, password, binding.edtConfirmCode.text.toString())
            }
            R.id.btn_resend -> {
                binding.btnResend.isEnabled = false
                binding.pbResend.visibility = View.VISIBLE
                viewModel.resendCode(username)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    companion object {

        private const val EXTRA_USERNAME = "_EXTRA.USERNAME"
        private const val EXTRA_PASSWORD = "_EXTRA.PASSWORD"
        private const val EXTRA_SHOULD_REQUEST_CODE = "_EXTRA.SHOULD_REQUEST_CODE"

        fun navigate(
            context: Context?,
            username: String,
            password: String,
            shouldRequestCode: Boolean = false
        ) {
            context?.startActivity(Intent(context, ActivityConfirmation::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
                putExtra(EXTRA_SHOULD_REQUEST_CODE, shouldRequestCode)
            })
        }
    }
}


