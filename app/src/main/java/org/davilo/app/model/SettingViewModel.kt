package org.davilo.app.model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.davilo.app.di.prefrence.AppPreferences


class SettingViewModel @ViewModelInject constructor(val appPreferences: AppPreferences) :
    ViewModel() {
    fun logOut() {
        appPreferences.setString(AppPreferences.Key.Token, null)
    }

    fun getEmail(): String? {
        return appPreferences.getString(AppPreferences.Key.Email, null)
    }


}