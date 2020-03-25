@file:JvmMultifileClass

package com.loginmodule.util

import java.util.*

/**
 * Common Utility Methods
 */

fun generateUUID(): String {
    return UUID.randomUUID().toString()
}