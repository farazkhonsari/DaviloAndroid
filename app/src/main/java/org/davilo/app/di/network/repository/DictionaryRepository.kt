package org.davilo.app.di.network.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.di.network.route.ApiInterface
import org.davilo.app.di.network.utils.MyException
import org.davilo.app.model.ResponseGetWordInformation
import org.davilo.app.utils.RxHelper
import javax.inject.Inject

class DictionaryRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getWordInformation(word: String?): Observable<ResponseGetWordInformation> {
        return apiInterface.getWordInformation(word = word)
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).doOnNext { res ->
                if (!res.status.success) {
                    throw MyException(res.status.status_message)
                }
            }.flatMap { response ->
                Observable.just(response.outputData)
            }
    }

//    fun getSuggestions(word: String): Observable<Int> {
//        return apiInterface.register(
//            RegisterInput(
//                email = email,
//                password = password,
//                profile = ProfileInput()
//            )
//        )
//            .subscribeOn(Schedulers.io())
//            .compose(RxHelper().addRegularRetryAndDelay())
//            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
//                if(!response.isSuccessful ){
//                    var output=Gson().fromJson(response.errorBody()?.string(),Output::class.java)
//                    Log.e("register","register"+output.status.status_message)
//                    throw ServerException(output)
//                }
//                Observable.just(1)
//            }
//    }
}


