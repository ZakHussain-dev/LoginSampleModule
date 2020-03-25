package com.loginmodule.util.datahelper

import androidx.room.Ignore
import com.loginmodule.data.ErrorScreenConstants

class ErrorResponse {

    var code: Int = -1

    var errorCode: Int = -1

    var businessErrorCode: Int = -1

    var message: String? = null

    var success: String? = null

    var status: String? = null

    @Ignore
    var errorScreenType: Int = ErrorScreenConstants.NO_SCREEN
}