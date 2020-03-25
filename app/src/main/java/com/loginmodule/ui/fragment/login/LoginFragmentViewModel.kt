package com.loginmodule.ui.fragment.login

import androidx.lifecycle.LiveData
import com.loginmodule.data.local.table.UserEntity
import com.loginmodule.data.remote.repository.UserRepository
import com.loginmodule.ui.base.BaseViewModel
import com.loginmodule.util.datahelper.DataResponse
import javax.inject.Inject

class LoginFragmentViewModel @Inject internal constructor(private val userRepository: UserRepository) :
    BaseViewModel() {

    fun loginUser(email: String, password: String): LiveData<DataResponse<UserEntity>> {
        return userRepository.loginUser(email, password)
    }
}