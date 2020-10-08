package org.davilo.app.ui.activity.login


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.di.network.repository.LoginRepository
import org.davilo.app.di.prefrence.AppPreferences
import org.davilo.app.model.LoginOutput

class LoginActivityViewModel @ViewModelInject constructor(
    val loginRepository: LoginRepository,
    val appPreferences: AppPreferences
) :
    ViewModel() {

    private var disposable: Disposable? = null

    val loginLiveData: MutableLiveData<LoginOutput> by lazy {
        MutableLiveData<LoginOutput>()
    }

    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    fun login(username: String, password: String) {
        appPreferences.setString(AppPreferences.Key.Email, username)
        disposable = loginRepository.login(email = username, password = password)
            .subscribe(loginLiveData::postValue, errorLiveData::postValue)

    }

    fun saveUserInfo(response: LoginOutput) {
        appPreferences.setString(AppPreferences.Key.Token, response.token)
        appPreferences.setString(AppPreferences.Key.UserId, response.user.id)

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}