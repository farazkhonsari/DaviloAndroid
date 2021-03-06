package org.davilo.app.model

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import org.davilo.app.di.network.repository.Repository

/**
 * Created by Abhinav Singh on 17,June,2020
 */
class HomeViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    var request: Disposable? = null
    val currentEnroll = MutableLiveData<Enroll>()
    val isLoading = MutableLiveData<Boolean>()
    val isNotEnrolled = MutableLiveData(false)
    fun loadCurrentEnroll() {

        if (request != null || currentEnroll.value != null) {
//            return
        } else {
            isLoading.value = true
        }
        request = repository.getCurrentEnroll()
            .subscribe(
                { (current_enroll) ->
                    currentEnroll.value = current_enroll
                    isLoading.value = false
                }
            ) { error: Throwable ->
                isLoading.value = false
                isNotEnrolled.value=true
                Log.e(
                    TAG,
                    "getPokemons: " + error.message
                )
            }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}