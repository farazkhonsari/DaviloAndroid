package org.davilo.app.ui.login

import io.reactivex.rxjava3.core.Observable
import org.davilo.app.model.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("./users/sign-in/")
    fun login(@Body loginInput: LoginInput): Observable<OutputArrat<LoginOutput>>


    @POST("./apps/get-current-enroll/")
    fun getCurrentEnroll(
        @Header("Authorization") Authorization: String?,
        @Body input: GetCurrentEnrollInput
    ): Observable<Output<GetCurrentEnrollOutput>>


}