package com.loginmodule.util.gravatar

/**
 * Easy Gravatar query building.
 *
 */
object Gravatar {

    const val MIN_IMAGE_SIZE_PIXEL = 1
    const val MAX_IMAGE_SIZE_PIXEL = 2048

    /**
     * Start a Gravatar URL building request using the specified email address.
     */
    fun with(email: String?): RequestBuilder {
        return RequestBuilder(email)
    }
}