package org.davilo.app.ui.login


import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.R
import org.davilo.app.model.AppPreferences
import org.davilo.app.model.LoginOutput

class LoginViewModel @ViewModelInject constructor(
    val loginRepository: LoginRepository,
    val appPreferences: AppPreferences
) :
    ViewModel() {


    private var username: String = ""
    private var password: String = ""
    private var loginObserver: Disposable? = null
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun login(username: String, password: String) {

        loginObserver = loginRepository.login(email = username, password = password)
            .subscribe(
                { response ->
                    loginObserver = null
                    onResponse(response)
                    invalidateLoginFormState()
                },
                { t ->
                    loginObserver = null
                    onFailure(t)
                    invalidateLoginFormState()

                })
        invalidateLoginFormState()

    }

    private fun onFailure(action: Throwable?) {

    }

    private fun onResponse(response: LoginOutput) {
        appPreferences.setString(AppPreferences.Key.Token, response.token)
        appPreferences.setString(AppPreferences.Key.UserId, response.user.id)
    }

    fun loginDataChanged(username: String, password: String) {
        this.username = username
        this.password = password
        invalidateLoginFormState()
    }

    fun invalidateLoginFormState() {
        if (appPreferences.getToken() != null) {
            _loginForm.value = LoginFormState(isSignedIn = true)
        } else if (loginObserver != null) {
            _loginForm.value = LoginFormState(isLoading = true)
        } else if (!isUserNameValid(username)) {
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

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}