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
class LevelListViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    var request: Disposable? = null
    val levels = MutableLiveData<ArrayList<Level>>()
    val isLoading = MutableLiveData<Boolean>()
    var isEnrollChanged = MutableLiveData<Boolean>()
    fun loadLevels() {
        if (levels.value != null || request != null) {

        } else {
            isLoading.value = true
        }
        request = repository.getLevelList()
            .subscribe(
                { response ->
                    request = null
                    isLoading.value = false
                    levels.setValue(
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

    fun enrollLevel(level: Level?) {

        repository.setEnroll(level?.id).subscribe(
            { response ->
                isEnrollChanged.value = true
            }
        ) { error: Throwable ->


        }

    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}