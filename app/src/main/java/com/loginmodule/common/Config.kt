@file:JvmName("Config")
@file:JvmMultifileClass

package com.loginmodule.common

import com.loginmodule.BuildConfig

/**
 * Check whether app is debug or release
 */
fun isRelease(): Boolean {
    return !BuildConfig.DEBUG
}
