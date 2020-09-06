package org.davilo.app.ui.login

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.RxHelper
import org.davilo.app.model.LoginInput
import org.davilo.app.model.LoginOutput
import org.davilo.app.model.ProfileInput
import org.davilo.app.model.RegisterInput
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
                Observable.just(1)
            }
    }
}
