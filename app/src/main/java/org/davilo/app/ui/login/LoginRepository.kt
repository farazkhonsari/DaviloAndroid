package org.davilo.app.ui.login

import io.reactivex.rxjava3.core.Observable
import org.davilo.app.model.LoginInput
import org.davilo.app.model.LoginOutput
import org.davilo.app.model.Output
import javax.inject.Inject

class LoginRepository @Inject constructor(val apiInterface: ApiInterface) {
    fun login(email: String, password: String): Observable<Output<LoginOutput>> {
        return apiInterface.login(LoginInput(email = email, password = password))
    }
}
