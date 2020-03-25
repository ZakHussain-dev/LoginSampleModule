package com.loginmodule.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * The model class which holds the top popular articles data
 */
data class UploadResponse(

    @SerializedName("avatar_url")
    var avatarUrl: String
)
