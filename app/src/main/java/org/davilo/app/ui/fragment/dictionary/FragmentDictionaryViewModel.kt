package org.davilo.app.ui.fragment.dictionary

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.davilo.app.di.network.repository.DictionaryRepository
import org.davilo.app.model.ResponseGetWordInformation
import retrofit2.HttpException
import java.util.concurrent.TimeUnit


class FragmentDictionaryViewModel @ViewModelInject constructor(
    val repository: DictionaryRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private val queryProcessor = PublishSubject.create<String?>()

    val wordInformationLiveData: MutableLiveData<Pair<String?, ResponseGetWordInformation>> by lazy {
        MutableLiveData<Pair<String?, ResponseGetWordInformation>>()
    }
    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    init {
        disposable = queryProcessor.debounce(300, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap { query ->
                repository.getWordInformation(word = query)
                    .onErrorReturn { error ->
                        if (error is HttpException) {
                            if (error.code() != 404) {
                                errorLiveData.value = error
                            }
                        } else {
                            errorLiveData.value = error
                        }
                        ResponseGetWordInformation()
                    }.map {
                        Pair(query, it)
                    }
            }
            .subscribe(wordInformationLiveData::postValue, errorLiveData::postValue)
    }

    fun query(word: String?) {
        queryProcessor.onNext(word)
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}