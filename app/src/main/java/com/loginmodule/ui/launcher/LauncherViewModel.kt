package com.loginmodule.ui.launcher

import com.loginmodule.data.prefs.AppPreference
import com.loginmodule.ui.base.BaseViewModel
import javax.inject.Inject

class LauncherViewModel @Inject internal constructor() : BaseViewModel() {

    fun getUserId(): String? {
        return AppPreference.getUserId()
    }

    fun saveUserId(userId: String) {
        AppPreference.saveUserId(userId)
    }
}