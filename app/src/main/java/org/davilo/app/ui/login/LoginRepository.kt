package org.davilo.app.ui.login

import android.util.Log
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.RxHelper
import org.davilo.app.model.*
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun login(email: String, password: String): Observable<LoginOutput> {
        return apiInterface.login(LoginInput(email = email, password = password))
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).doOnNext { res ->
                if (!res.status.success) {
                    throw MyException(res.status.status_message)
                }
            }.flatMap { response ->
                Observable.just(response.outputData!![0])
            }
    }

    fun register(email: String, password: String): Observable<Int> {
        return apiInterface.register(
            RegisterInput(
                email = email,
                password = password,
                profile = ProfileInput()
            )
        )
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                if(!response.isSuccessful ){
                    var output=Gson().fromJson(response.errorBody()?.string(),Output::class.java)
                    Log.e("register","register"+output.status.status_message)
                    throw ServerException(output)
                }
                Observable.just(1)
            }
    }
}


