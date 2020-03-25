package com.loginmodule.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * The model class which holds the top popular articles data
 */
data class LoginResponse(

    @SerializedName("userid")
    var userid: String,
    @SerializedName("token")
    var access_token: String

)
