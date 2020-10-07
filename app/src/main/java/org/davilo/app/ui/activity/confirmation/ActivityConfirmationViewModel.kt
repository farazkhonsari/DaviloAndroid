package org.davilo.app.ui.activity.confirmation


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.di.network.repository.LoginRepository
import org.davilo.app.di.prefrence.AppPreferences
import org.davilo.app.model.LoginOutput

class ActivityConfirmationViewModel @ViewModelInject constructor(
    val loginRepository: LoginRepository,
    val appPreferences: AppPreferences
) : ViewModel() {


    private var disposable: Disposable? = null

    private var disposable2: Disposable? = null

    val confirmEmailLiveData: MutableLiveData<LoginOutput> by lazy {
        MutableLiveData<LoginOutput>()
    }

    val resendConfirmEmailLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    fun confirm(email: String?, password: String?, code: String) {

        disposable = loginRepository.confirm(email, code)
            .flatMap {
                loginRepository.login(email = email, password = password)
            }
            .subscribe(confirmEmailLiveData::postValue, errorLiveData::postValue)

    }

    fun resendCode(email: String?) {

        disposable = loginRepository.resendCode(email)
            .subscribe(resendConfirmEmailLiveData::postValue, errorLiveData::postValue)

    }

    fun saveRemainingTimeInSharedPreferences(remainingTime: Long) {
        appPreferences.setString(
            AppPreferences.Key.ConfirmationRemainingTime,
            remainingTime.toString()
        )
    }

    fun saveUserInfo(response: LoginOutput) {
        appPreferences.setString(AppPreferences.Key.Token, response.token)
        appPreferences.setString(AppPreferences.Key.UserId, response.user.id)
        appPreferences.setString(AppPreferences.Key.Email, response.user.id)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        disposable2?.dispose()
    }

}