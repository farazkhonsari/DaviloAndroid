package org.davilo.app.di.network.repository

import android.util.Log
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.di.network.route.ApiInterface
import org.davilo.app.di.network.utils.MyException
import org.davilo.app.di.network.utils.ServerException
import org.davilo.app.model.*
import org.davilo.app.utils.RxHelper
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun login(email: String?, password: String?): Observable<LoginOutput> {
        return apiInterface.login(
            LoginInput(
                email = email,
                password = password
            )
        )
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                if (!response.isSuccessful) {
                    var output = Gson().fromJson(response.errorBody()?.string(), Output::class.java)
                    Log.e("login", "login" + output.status.status_message)
                    throw ServerException(output)
                }
                Observable.just(response?.body()?.outputData?.get(0)!!)
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
                if (!response.isSuccessful) {
                    var output = Gson().fromJson(response.errorBody()?.string(), Output::class.java)
                    Log.e("register", "register" + output.status.status_message)
                    throw ServerException(output)
                }
                Observable.just(1)
            }
    }

    fun confirm(email: String?, code: String?): Observable<Boolean> {
        return apiInterface.confirmEmail(
            ConfirmEmailInput(
                email = email,
                code = code
            )
        )
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                if (!response.isSuccessful) {
                    var output = Gson().fromJson(response.errorBody()?.string(), Output::class.java)
                    Log.e("confirm", "confirm" + output.status.status_message)
                    throw ServerException(output)
                }
                Observable.just(true)
            }
    }

    fun resendCode(email: String?): Observable<Boolean> {
        return apiInterface.sendConfirmationEmail(
            SendConfirmationEmailInput(
                email
            )
        )
            .subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).doOnNext { res ->
                if (!res.status.success) {
                    throw MyException(res.status.status_message)
                }
            }.flatMap {
                Observable.just(true)
            }
    }
}


