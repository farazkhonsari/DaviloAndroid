package org.davilo.app.di.prefrence

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferences @Inject constructor(@ApplicationContext context: Context) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences("app", 0)

    enum class Key {
        Token,
        Email,
        UserId,
        ConfirmationRemainingTime
    }

    fun getToken(): String? {
        return getString(Key.Token)
    }

    fun getUserId(): String? {
        return getString(Key.UserId)
    }

    fun setString(key: Key, value: String?) {
        sharedPreferences.edit().putString(key.toString(), value).apply()
    }

    fun getString(key: Key, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key.toString(), defaultValue)
    }

}