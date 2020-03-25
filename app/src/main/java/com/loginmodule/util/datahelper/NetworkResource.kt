package com.loginmodule.util.datahelper

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.loginmodule.data.ErrorCodeConstants
import com.loginmodule.data.ErrorScreenConstants

abstract class NetworkResource<ApiResponseType>
@MainThread
protected constructor() {

    val apiResultLiveData: MediatorLiveData<DataResponse<ApiResponseType>> = MediatorLiveData()

    init {
        fetchFromNetwork()
    }

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract fun createCall(): LiveData<DataResponse<ApiResponseType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    private fun onFetchFailed(errorCode: Int, errorResponse: ErrorResponse?) {
        apiResultLiveData.value = DataResponse(errorCode, errorResponse!!)
    }

    private fun fetchFromNetwork() {
        apiResultLiveData.value = DataResponse(DataResponse.Status.LOADING)
        val apiResponse = createCall()
        apiResultLiveData.addSource(apiResponse) { response ->
            if (response != null) {
                if (response.data != null) {
                    apiResultLiveData.removeSource(apiResponse)
                    apiResultLiveData.setValue(response)
                } else {
                    onFetchFailed(response.errorCode, response.errorResponse)
                }
            } else {
                sendNetworkFailure()
            }
        }
    }

    private fun sendNetworkFailure() {
        val errorResponse = ErrorResponse()
        errorResponse.errorCode = ErrorCodeConstants.NETWORK
        errorResponse.errorScreenType =
            ErrorScreenConstants.NO_INTERNET

        onFetchFailed(ErrorCodeConstants.NETWORK, errorResponse)
    }
}