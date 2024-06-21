package com.dicoding.kelana.db

import android.content.Context
import com.dicoding.kelana.data.UserModel

class UserPreference (context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val TOKEN = "token"
        private const val PREFERENCE = "preference"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.usename)
        editor.putString(EMAIL, value.email)
        editor.putString(PASSWORD, value.password)
        editor.putString(TOKEN, value.token)
        editor.putString(PREFERENCE, value.preference)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.usename = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.password = preferences.getString(PASSWORD, "")
        model.token = preferences.getString(TOKEN, "")
        model.preference = preferences.getString(PREFERENCE, "")

        return model
    }

    fun isUserLoggedIn(): Boolean {
        val user = getUser()
        return !user.usename.isNullOrEmpty() &&
                !user.email.isNullOrEmpty() &&
                !user.password.isNullOrEmpty() &&
                !user.preference.isNullOrEmpty()
    }
}