package org.davilo.app.model

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.davilo.app.Repository

/**
 * Created by Abhinav Singh on 17,June,2020
 */
class HomeViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {
    val currentEnroll = MutableLiveData<Enroll>()

    fun loadCurrentEnroll() {
        repository.getCurrentEnroll()
            .subscribe(
                { (current_enroll) ->
                    currentEnroll.setValue(
                        current_enroll
                    )
                }
            ) { error: Throwable ->
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