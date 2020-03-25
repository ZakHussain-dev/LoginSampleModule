package com.loginmodule.data.prefs

import com.loginmodule.util.Prefs

object AppPreference {

    fun saveUserId(userId: String?) {
        Prefs.putString(KEY_USER_ID, userId)
    }

    fun getUserId(): String? {
        return Prefs.getString(KEY_USER_ID)
    }
}