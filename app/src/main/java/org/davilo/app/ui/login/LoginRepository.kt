package org.davilo.app.ui.login

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.model.LoginInput
import org.davilo.app.model.LoginOutput
import org.davilo.app.model.Output
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface) {
    fun login(email: String, password: String): Observable<LoginOutput> {
        return apiInterface.login(LoginInput(email = email, password = password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData[0])
            }
    }
}
