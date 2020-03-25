package com.loginmodule.data.remote

import androidx.lifecycle.LiveData
import com.loginmodule.data.local.table.UserEntity
import com.loginmodule.data.remote.model.LoginResponse
import com.loginmodule.data.remote.model.UploadResponse
import com.loginmodule.util.datahelper.DataResponse
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Provide request and response type information of REST APIS
 */
interface ApiService {

    @POST(GET_USER)
    fun getUser(@QueryMap map: Map<String, String>): LiveData<DataResponse<UserEntity>>

    @GET(NEW_USER)
    fun loginUser(@QueryMap map: Map<String, String>): LiveData<DataResponse<LoginResponse>>

    @Multipart
    @JvmSuppressWildcards
    @POST(UPLOAD_IMAGE)
    fun uploadUserImage(@PartMap map: Map<String, RequestBody>): LiveData<DataResponse<UploadResponse>>

}