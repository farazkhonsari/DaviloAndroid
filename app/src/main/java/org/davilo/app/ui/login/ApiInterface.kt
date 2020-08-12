package org.davilo.app.ui.login

import io.reactivex.rxjava3.core.Observable
import org.davilo.app.model.LoginInput
import org.davilo.app.model.LoginOutput
import org.davilo.app.model.Output
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("./users/sign-in/")
    fun login(@Body loginInput: LoginInput): Observable<Output<LoginOutput>>
}