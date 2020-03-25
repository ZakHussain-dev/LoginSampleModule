package com.loginmodule.ui.fragment.profile

import androidx.lifecycle.LiveData
import com.loginmodule.data.local.table.UserEntity
import com.loginmodule.data.prefs.AppPreference
import com.loginmodule.data.remote.model.UploadResponse
import com.loginmodule.data.remote.repository.UserRepository
import com.loginmodule.ui.base.BaseViewModel
import com.loginmodule.util.datahelper.DataResponse
import javax.inject.Inject

class ProfileFragmentViewModel @Inject internal constructor(private val userRepository: UserRepository) :
    BaseViewModel() {

    fun getUserId(): String? {
        return AppPreference.getUserId()
    }

    fun logoutUser() {
        AppPreference.saveUserId(null)
        userRepository.clearUser()
    }

    fun getUser(userId: String): LiveData<DataResponse<UserEntity>> {
        return userRepository.getUser(userId)
    }

    fun getUserImageUrl(imageUrl: String): LiveData<DataResponse<UploadResponse>> {
        return userRepository.getUserImageUrl(imageUrl)
    }

    fun updateUserImage(userId: String, imageUrl: String) {
        userRepository.updateUserImageUrl(userId, imageUrl)
    }
}