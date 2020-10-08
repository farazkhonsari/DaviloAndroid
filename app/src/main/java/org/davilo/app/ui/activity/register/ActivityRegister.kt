package org.davilo.app.ui.activity.register

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
import org.davilo.app.databinding.ActivityRegisterBinding
import org.davilo.app.di.network.utils.ServerException
import org.davilo.app.ui.activity.confirmation.ActivityConfirmation
import org.davilo.app.ui.base.BaseActivity
import org.davilo.app.utils.afterTextChanged

@AndroidEntryPoint
class ActivityRegister : BaseActivity<ActivityRegisterBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_register

    private val viewModel: ActivityRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()

        observeLiveData()

    }

    private fun initViews() {

        binding.register.isEnabled = false

        binding.username.afterTextChanged {
            binding.register.isEnabled = validateInputs()
        }

        binding.password.afterTextChanged {
            binding.register.isEnabled = validateInputs()
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    binding.register.performClick()
            }
            false
        }

        binding.register.setOnClickListener(this)
    }

    private fun observeLiveData() {
        viewModel.registerLiveData.observe(this, Observer {
            binding.loading.visibility = View.GONE
            binding.register.isEnabled = true
            binding.username.isEnabled = true
            binding.password.isEnabled = true


            ActivityConfirmation.navigate(
                context = this,
                username = binding.username.text.toString(),
                password = binding.password.text.toString()
            )
        })
        viewModel.errorLiveData.observe(this, Observer { error ->
            binding.loading.visibility = View.GONE
            binding.register.isEnabled = true
            binding.username.isEnabled = true
            binding.password.isEnabled = true
            if (error is ServerException) {
                binding.password.error = error.output?.status?.status_message
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.register -> {
                binding.loading.visibility = View.VISIBLE
                binding.register.isEnabled = false
                binding.username.isEnabled = false
                binding.password.isEnabled = false
                viewModel.register(
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
            context?.startActivity(Intent(context, ActivityRegister::class.java))
        }
    }
}


