package org.davilo.app.model

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

public class AppPreferences @Inject constructor(@ApplicationContext context: Context) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences("app", 0)

    enum class Key {
        Token,
        UserId,
    }

    fun getToken(): String? {
        return getString(Key.Token)
    }

    fun getUserId(): String? {
        return getString(Key.UserId)
    }

    public fun setString(key: Key, value: String?) {
        sharedPreferences.edit().putString(key.toString(), value).apply()
    }

    public fun getString(key: Key, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key.toString(), defaultValue)
    }

}