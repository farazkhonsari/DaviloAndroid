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

    @POST("./apps/get-enroll-info/")
    fun getCategories(
        @Header("Authorization") Authorization: String?,
        @Body input: GetCategoryListInput
    ): Observable<Output<GetCategoryListOutput>>

    @POST("./apps/get-enroll-info/")
    fun getAppsOfCategory(
        @Header("Authorization") Authorization: String?,
        @Body input: GetAppsListInput
    ): Observable<Output<GetAppsListOutput>>

    @POST("./apps/get-enroll-info/")
    fun getAllLevelList(
        @Header("Authorization") Authorization: String?,
        @Body input: GetLevelListInput
    ): Observable<Output<GetLevelListOutput>>

    @POST("./apps/set-enroll/")
    fun setEnroll(
        @Header("Authorization") Authorization: String?,
        @Body input: SetEnrollInput
    ): Observable<Output<SetEnrollOutput>>

    @POST("./apps/complete-app/")
    fun completeApp(
        @Header("Authorization") Authorization: String?,
        @Header("X-Timezone") xTimezone: String? = "Asia/Tehran",
        @Body input: CompleteAppInput
    ): Observable<Output<CompleteAppOutput>>


}