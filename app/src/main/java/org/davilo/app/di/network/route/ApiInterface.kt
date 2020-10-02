package org.davilo.app.di.network.route

import io.reactivex.rxjava3.core.Observable
import org.davilo.app.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("./users/sign-in/")
    fun login(@Body loginInput: LoginInput): Observable<OutputArrat<LoginOutput>>

    @POST("./users/sign-up/")
    fun register(@Body input: RegisterInput): Observable<Response<OutputArrat<RegisterOutput>>>


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


    @GET("dictionary/get-new-word-information/{word}/")
    fun getWordInformation(
        @Path("word") word: String?
    ): Observable<Output<ResponseGetWordInformation>>
}