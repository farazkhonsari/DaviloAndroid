package org.davilo.app

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.model.AppPreferences
import org.davilo.app.model.GetCurrentEnrollInput
import org.davilo.app.model.GetCurrentEnrollOutput
import org.davilo.app.ui.login.ApiInterface
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val appPreferences: AppPreferences
) {
    fun getCurrentEnroll(): Observable<GetCurrentEnrollOutput> {
        return apiInterface.getCurrentEnroll(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            GetCurrentEnrollInput(user_id = appPreferences.getUserId())
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData)
            }
    }
}
