package org.davilo.app

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.davilo.app.model.*
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
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData)
            }
    }

    fun getCategoryList(moduleId: String): Observable<ArrayList<Category>> {
        return apiInterface.getCategories(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            GetCategoryListInput(user_id = appPreferences.getUserId(), object_id = moduleId)
        ).subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData!!.module.categories)
            }
    }

    fun getAppList(categoryId: String): Observable<ArrayList<App>> {
        return apiInterface.getAppsOfCategory(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            GetAppsListInput(user_id = appPreferences.getUserId(), object_id = categoryId)
        ).subscribeOn(Schedulers.io())

            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData!!.category.apps)
            }
    }

    fun getLevelList(): Observable<ArrayList<Level>> {
        return apiInterface.getAllLevelList(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            GetLevelListInput(user_id = appPreferences.getUserId())
        ).subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap { response ->
                Observable.just(response.outputData!!.levels)
            }
    }

    fun setEnroll(levelId: String?): Observable<Int> {
        return apiInterface.setEnroll(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            SetEnrollInput(user_id = appPreferences.getUserId(), level_id = levelId)
        ).subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap {
                Observable.just(1)
            }
    }

    fun completeApp(appId: String?): Observable<Int> {
        return apiInterface.completeApp(
            Authorization = "Bearer" + " " + appPreferences.getToken(), input =
            CompleteAppInput(app_id = appId)
        ).subscribeOn(Schedulers.io())
            .compose(RxHelper().addRegularRetryAndDelay())
            .observeOn(AndroidSchedulers.mainThread()).flatMap {
                Observable.just(1)
            }
    }
}
