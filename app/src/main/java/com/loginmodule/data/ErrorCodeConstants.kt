package com.loginmodule.data

internal object ErrorCodeConstants {
    const val WFH = -1
    const val NETWORK = 0
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED_TOKEN = 401
    const val NOT_FOUND = 404
    const val FORBIDDEN = 403
    const val TOO_MANY_REQUEST = 420
    const val FORCE_UPGRADE = 426
    const val SERVER_BUG = 500
    const val SERVER_MAINTENANCE = 502
}
