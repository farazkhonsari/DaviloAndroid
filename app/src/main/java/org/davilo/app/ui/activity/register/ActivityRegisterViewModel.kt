package org.davilo.app.ui.activity.register


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.di.network.repository.LoginRepository
import org.davilo.app.di.prefrence.AppPreferences

class ActivityRegisterViewModel @ViewModelInject constructor(
    val loginRepository: LoginRepository,
    val appPreferences: AppPreferences
) :
    ViewModel() {

    private var disposable: Disposable? = null

    val registerLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    fun register(username: String, password: String) {

        disposable = loginRepository.register(email = username, password = password)
            .subscribe(
                registerLiveData::postValue,
                errorLiveData::postValue
            )

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}