package org.davilo.app.model

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.Repository

/**
 * Created by Abhinav Singh on 17,June,2020
 */
class AppListViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    var request: Disposable? = null
    val apps = MutableLiveData<ArrayList<App>>()
    val isLoading = MutableLiveData<Boolean>()
    fun loadCurrentEnroll(objectId: String) {
        apps.value = ArrayList()
        isLoading.value = true
        request = repository.getAppList(objectId)
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