package org.davilo.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.EntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


import org.davilo.app.R
import org.davilo.app.model.LoginOutput
import org.davilo.app.model.Output

class LoginViewModel @ViewModelInject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun login(username: String, password: String) {
        loginRepository.login(email = username, password = password).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> onResponse(response) },
                { t -> onFailure(t) })

    }

    private fun onFailure(action: Throwable?) {
        println("error")

    }

    private fun onResponse(response: Output<LoginOutput>?) {
        println("response")
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}