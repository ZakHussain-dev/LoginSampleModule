package com.loginmodule.ui.fragment.splash

import androidx.lifecycle.LiveData
import com.loginmodule.data.local.table.UserEntity
import com.loginmodule.data.prefs.AppPreference
import com.loginmodule.data.remote.repository.UserRepository
import com.loginmodule.ui.base.BaseViewModel
import com.loginmodule.util.datahelper.DataResponse
import javax.inject.Inject

class SplashViewModel @Inject internal constructor(private val userRepository: UserRepository) : BaseViewModel() {
    fun getUserId(): String? {
        return AppPreference.getUserId()
    }

    fun saveUserId(userId: String?) {
        AppPreference.saveUserId(userId)
    }

    fun getUser(userId: String): LiveData<DataResponse<UserEntity>> {
        return userRepository.getUser(userId)
    }
}