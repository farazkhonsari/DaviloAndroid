package org.davilo.app.ui.activity.webApp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.davilo.app.di.network.repository.Repository
import org.davilo.app.di.prefrence.AppPreferences


class ActivityWebAppViewModel @ViewModelInject constructor(
    val repository: Repository,
    val appPreferences: AppPreferences
) :
    ViewModel() {


    val completeAppLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    fun completeApp(appId: String?) {
        repository.completeApp(appId)
            .subscribe(completeAppLiveData::postValue, errorLiveData::postValue)

    }

}