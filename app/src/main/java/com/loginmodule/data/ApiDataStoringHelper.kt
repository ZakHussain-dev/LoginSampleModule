package com.loginmodule.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.loginmodule.util.datahelper.DataResponse

interface ApiDataStoringHelper<ApiResponseType, DbResponseType> {

    val resultLiveData: MediatorLiveData<DataResponse<DbResponseType>>

    // Called to save the result of the API response into the database
    @WorkerThread
    suspend fun storeApiResult(response: ApiResponseType)

    // Called to get the cached data from the database
    @MainThread
    fun loadFromDb(): LiveData<DbResponseType>
}
