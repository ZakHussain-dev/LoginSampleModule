package com.loginmodule.data.remote.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.loginmodule.common.REQ_AVATAR
import com.loginmodule.common.REQ_EMAIL
import com.loginmodule.common.REQ_PASSWORD
import com.loginmodule.common.REQ_USER_ID
import com.loginmodule.data.local.AppDatabase
import com.loginmodule.data.local.table.UserEntity
import com.loginmodule.data.prefs.AppPreference
import com.loginmodule.data.remote.ApiService
import com.loginmodule.data.remote.CommonParams
import com.loginmodule.data.remote.MultipartParams
import com.loginmodule.data.remote.model.LoginResponse
import com.loginmodule.data.remote.model.UploadResponse
import com.loginmodule.util.datahelper.DataResponse
import com.loginmodule.util.datahelper.DbResource
import com.loginmodule.util.datahelper.NetworkDbBindingResource
import com.loginmodule.util.datahelper.NetworkResource
import com.loginmodule.util.gravatar.Gravatar
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Helper Class for getting data from network or local database
 */
@Singleton
open class UserRepository @Inject internal constructor(
    @Named("application_context")
    private val baseContext: Context,
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) {

    fun loginUser(email: String, password: String): LiveData<DataResponse<UserEntity>> {
        val commonParams = CommonParams.Builder()
            .add(REQ_EMAIL, email)
            .add(REQ_PASSWORD, password)
            .build()

        return object : NetworkDbBindingResource<LoginResponse, UserEntity>() {

            override fun loadFromDb(): LiveData<UserEntity> {
                return appDatabase.userDao().loadUserByEmailId(email)
            }

            override fun createCall(): LiveData<DataResponse<LoginResponse>> {
                return apiService.loginUser(commonParams.map)
            }

            override fun shouldRefreshData(data: UserEntity?): Boolean {
                return data == null
            }

            override fun shouldAlwaysFetchData(): Boolean {
                return false
            }

            override suspend fun storeApiResult(response: LoginResponse) {
                val gravatarUrl =
                    Gravatar.with(email).size(Gravatar.MAX_IMAGE_SIZE_PIXEL)
                        .build()
                Timber.e("Gravatar Found :: $gravatarUrl")
                val userEntity = UserEntity(response.userid, email, password, gravatarUrl)
                appDatabase.userDao().saveUser(userEntity)
                AppPreference.saveUserId(response.userid)
            }
        }.resultLiveData
    }

    fun getUser(userId: String): LiveData<DataResponse<UserEntity>> {
        val commonParams = CommonParams.Builder()
            .add(REQ_USER_ID, userId)
            .build()


        return object : NetworkDbBindingResource<UserEntity, UserEntity>() {

            override fun loadFromDb(): LiveData<UserEntity> {
                return appDatabase.userDao().loadUserByUserId(userId)
            }

            override fun createCall(): LiveData<DataResponse<UserEntity>> {
                return apiService.getUser(commonParams.map)
            }

            override fun shouldRefreshData(data: UserEntity?): Boolean {
                return data == null
            }

            override fun shouldAlwaysFetchData(): Boolean {
                return false
            }

            override suspend fun storeApiResult(response: UserEntity) {
                AppPreference.saveUserId(response.userId)
            }
        }.resultLiveData
    }

    fun clearUser() {
        object : DbResource() {
            override suspend fun readOrWriteDb() {
                appDatabase.userDao().deleteUser()
            }
        }
    }

    fun getUserImageUrl(image: String): LiveData<DataResponse<UploadResponse>> {
        val multipartParams = MultipartParams.Builder()
            .addFile(REQ_AVATAR, File(image))
            .build()

        return object : NetworkResource<UploadResponse>() {
            override fun createCall(): LiveData<DataResponse<UploadResponse>> {
                return apiService.uploadUserImage(multipartParams.map)
            }
        }.apiResultLiveData
    }

    fun updateUserImageUrl(userId: String, image: String) {
        object : DbResource() {
            override suspend fun readOrWriteDb() {
                appDatabase.userDao().updateUserImage(userId, image)
            }
        }
    }
}