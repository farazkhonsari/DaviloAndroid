package org.davilo.app.model

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.di.network.repository.Repository
import org.davilo.app.utils.NotificationCenter

/**
 * Created by Abhinav Singh on 17,June,2020
 */
class AppListViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    var request: Disposable? = null
    val apps = MutableLiveData<ArrayList<App>>()
    val appsMap = HashMap<String, App>()
    val isLoading = MutableLiveData<Boolean>()
    var delegate = NotificationCenter.NotificationCenterDelegate { id, args ->
        if (id == NotificationCenter.appDone) {
            val appId = args[0]
            appsMap[appId]?.done = true
            apps.value = apps.value
        }
    }

    init {
        NotificationCenter.getInstance().addObserver(delegate, NotificationCenter.appDone)
    }

    override fun onCleared() {
        NotificationCenter.getInstance().removeObserver(delegate, NotificationCenter.appDone)
        super.onCleared()
    }

    fun loadCurrentEnroll(objectId: String) {
        apps.value = ArrayList()
        isLoading.value = true
        request = repository.getAppList(objectId).doOnNext { response ->
            for (app in response) {
                if (app.id != null) {
                    appsMap[app.id!!] = app
                    app.categoryId = objectId
                }
            }
        }
            .subscribe(
                { response ->
                    request = null
                    isLoading.value = false
                    apps.setValue(
                        response
                    )

                }
            ) { error: Throwable ->
                Log.e(
                    TAG,
                    "getPokemons: " + error.message
                )
                request = null
                isLoading.value = false
            }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}