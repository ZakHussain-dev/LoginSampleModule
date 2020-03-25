package com.loginmodule.util.datahelper

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.loginmodule.BuildConfig
import com.loginmodule.data.ErrorCodeConstants
import com.loginmodule.data.ErrorScreenConstants
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class LiveDataCallAdapter<R> internal constructor(private val responseType: Type) :
    CallAdapter<R, LiveData<DataResponse<R>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(@NonNull call: Call<R>): LiveData<DataResponse<R>> {

        return object : LiveData<DataResponse<R>>() {
            override fun onActive() {
                super.onActive()

                call.clone().enqueue(object : Callback<R> {
                    override fun onResponse(
                        @NonNull call1: Call<R>,
                        @NonNull response: Response<R>
                    ) {
                        if (call1.isCanceled) return

                        if (response.isSuccessful) {
                            postValue(DataResponse(response.body()!!))
                        } else {
                            var errorResponse = ErrorResponse()

                            if (response.errorBody() != null) {
                                errorResponse = getErrorResponse(response.errorBody()!!)
                            }
                            errorResponse.errorCode = response.code()
                            errorResponse.errorScreenType = getErrorScreen(
                                errorResponse.errorCode,
                                errorResponse.businessErrorCode
                            )
                            postValue(DataResponse(response.code(), errorResponse))

                        }
                    }

                    override fun onFailure(@NonNull call1: Call<R>, @NonNull t: Throwable) {
                        if (BuildConfig.DEBUG) {
                            Timber.e(t)
                        }
                        if (call1.isCanceled) return
                        val errorResponse = ErrorResponse()

                        if (t is IOException || t is SocketTimeoutException) {
                            val networkErrorCode = ErrorCodeConstants.NETWORK
                            errorResponse.errorCode = networkErrorCode
                            errorResponse.errorScreenType = getErrorScreen(
                                errorResponse.errorCode,
                                errorResponse.businessErrorCode
                            )
                            postValue(DataResponse(networkErrorCode, errorResponse))
                        } else {
                            val badReqErrorCode = ErrorCodeConstants.BAD_REQUEST
                            errorResponse.errorCode = badReqErrorCode
                            errorResponse.errorScreenType = getErrorScreen(
                                errorResponse.errorCode,
                                errorResponse.businessErrorCode
                            )
                            postValue(DataResponse(badReqErrorCode, errorResponse))
                        }
                    }
                })
            }
        }
    }

    private fun getErrorScreen(errorCode: Int, businessErrorCode: Int): Int {
        return when (errorCode) {
            ErrorCodeConstants.FORCE_UPGRADE -> ErrorScreenConstants.FORCE_UPDATE
            ErrorCodeConstants.UNAUTHORIZED_TOKEN -> ErrorScreenConstants.UNAUTHORISED
            ErrorCodeConstants.NETWORK -> ErrorScreenConstants.NO_INTERNET
            ErrorCodeConstants.WFH,
            ErrorCodeConstants.SERVER_MAINTENANCE,
            ErrorCodeConstants.SERVER_BUG,
            ErrorCodeConstants.TOO_MANY_REQUEST,
            ErrorCodeConstants.FORBIDDEN,
            ErrorCodeConstants.NOT_FOUND -> ErrorScreenConstants.GENERIC
            else -> ErrorScreenConstants.GENERIC
        }
    }

    private fun getErrorResponse(responseBody: ResponseBody): ErrorResponse {
        try {
            return Gson().fromJson(responseBody.string(), ErrorResponse::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ErrorResponse()

    }

}
