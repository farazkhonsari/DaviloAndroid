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
class SharedViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    var request: Disposable? = null
    val categories = MutableLiveData<ArrayList<Category>>()
    val isLoading = MutableLiveData<Boolean>()

    fun loadCurrentEnroll(objectId: String) {
        if (categories.value != null || request != null) {

        }else{
            isLoading.value = true
        }

        request = repository.getCategoryList(objectId)
            .subscribe(
                { response ->
                    request = null
                    isLoading.value = false
                    categories.setValue(
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