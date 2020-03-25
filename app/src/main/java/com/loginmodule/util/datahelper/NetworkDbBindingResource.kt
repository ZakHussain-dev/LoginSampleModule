package com.loginmodule.util.datahelper

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.MediatorLiveData
import com.loginmodule.data.ApiDataStoringHelper
import com.loginmodule.data.ErrorCodeConstants
import com.loginmodule.data.ErrorScreenConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class NetworkDbBindingResource<ApiResponseType, DbResponseType>
@MainThread
protected constructor() : NetworkResource<ApiResponseType>(),
    ApiDataStoringHelper<ApiResponseType, DbResponseType> {
    // returns a LiveData that represents the resource
    final override val resultLiveData: MediatorLiveData<DataResponse<DbResponseType>> =
        MediatorLiveData()

    init {
        resultLiveData.value = DataResponse(DataResponse.Status.LOADING)
        if (this.shouldAlwaysFetchData()) {
            fetchFromNetwork()
        } else {
            val dbSource = this.loadFromDb()
            resultLiveData.addSource(dbSource) { data ->
                if (shouldRefreshData(data)) {
                    resultLiveData.removeSource(dbSource)
                    fetchFromNetwork()
                } else {
                    resultLiveData.setValue(DataResponse(data))
                }
            }
        }
    }

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract fun shouldRefreshData(@Nullable data: DbResponseType?): Boolean

    @MainThread
    protected abstract fun shouldAlwaysFetchData(): Boolean

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    private fun onFetchFailed(errorCode: Int, errorResponse: ErrorResponse?) {
        resultLiveData.value = DataResponse(errorCode, errorResponse!!)
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        resultLiveData.addSource(apiResponse) { response ->
            if (response != null) {
                if (response.data != null) {
                    resultLiveData.removeSource(apiResponse)
                    CoroutineScope(Dispatchers.IO).launch {
                        storeApiResult(response.data)
                        val liveData = loadFromDb()
                        withContext(Dispatchers.Main) {
                            resultLiveData.addSource(liveData) { newData ->
                                resultLiveData.setValue(DataResponse(newData))
                            }
                        }
                    }
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
