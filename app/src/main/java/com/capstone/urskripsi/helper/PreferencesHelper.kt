package com.capstone.urskripsi.helper

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun setDataBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun setDataString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getDataBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getDataString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clearData() {
        editor.clear().apply()
    }

    companion object {
        private const val PREF_NAME = "user_pref"
    }

}