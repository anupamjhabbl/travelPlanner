package com.example.bbltripplanner.common.infra

import android.content.Context
import android.content.SharedPreferences
import com.example.bbltripplanner.common.entity.User
import com.google.gson.Gson

class PreferenceManager (
    val context: Context,
    val gson: Gson
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val LOCAL_USER_DATA = "local_user_data"
        private const val PREFS_NAME = "basic_prefs"
    }

    fun saveLocalUser(user: User) {
        sharedPreferences.edit().putString(LOCAL_USER_DATA, gson.toJson(user)).apply()
    }

    fun removeLocalUser() {
        sharedPreferences.edit().remove(LOCAL_USER_DATA).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun getLocalUser(): User? {
        val userData = sharedPreferences.getString(LOCAL_USER_DATA, null)
        return userData?.let {
            gson.fromJson(userData, User::class.java)
        }
    }
}