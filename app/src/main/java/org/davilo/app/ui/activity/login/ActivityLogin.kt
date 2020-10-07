package org.davilo.app.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.ActivityLoginBinding
import org.davilo.app.di.network.utils.ServerException
import org.davilo.app.ui.activity.confirmation.ActivityConfirmation
import org.davilo.app.ui.activity.main.ActivityMain
import org.davilo.app.ui.base.BaseActivity
import org.davilo.app.utils.afterTextChanged

@AndroidEntryPoint
class ActivityLogin : BaseActivity<ActivityLoginBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_login

    private val viewModel: LoginActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        observeLiveData()

    }

    private fun initViews() {

        binding.login.isEnabled = false

        binding.username.afterTextChanged {
            binding.login.isEnabled = validateInputs()
        }

        binding.password.afterTextChanged {
            binding.login.isEnabled = validateInputs()
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    binding.login.performClick()
            }
            false
        }

        binding.login.setOnClickListener(this)

    }

    private fun observeLiveData() {
        viewModel.loginLiveData.observe(this, Observer { response ->
            binding.loading.visibility = View.GONE
            binding.login.isEnabled = true
            binding.username.isEnabled = true
            binding.password.isEnabled = true
            viewModel.saveUserInfo(response)
            finishAffinity()
            ActivityMain.navigate(this)
        })

        viewModel.errorLiveData.observe(this, Observer { error ->
            binding.loading.visibility = View.GONE
            binding.login.isEnabled = true
            binding.username.isEnabled = true
            binding.password.isEnabled = true
            if (error is ServerException) {
                if (error.output?.status?.status_message == "Email has not validate") {
                    ActivityConfirmation.navigate(
                        context = this,
                        username = binding.username.text.toString(),
                        password = binding.password.text.toString(),
                        shouldRequestCode = true
                    )
                } else {
                    binding.password.error = error.output?.status?.status_message
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.login -> {
                binding.loading.visibility = View.VISIBLE
                binding.login.isEnabled = false
                binding.username.isEnabled = false
                binding.password.isEnabled = false
                viewModel.login(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }
        }
    }

    private fun validateInputs(): Boolean {

        if (!isUserNameValid(binding.username.text.toString())) {
            binding.username.error = getString(R.string.invalid_username)
            return false
        }

        if (!isPasswordValid(binding.password.text.toString())) {
            binding.password.error = getString(R.string.invalid_password)
            return false
        }

        return true
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }

    companion object {

        fun navigate(context: Context?) {
            context?.startActivity(Intent(context, ActivityLogin::class.java))
        }
    }
}
