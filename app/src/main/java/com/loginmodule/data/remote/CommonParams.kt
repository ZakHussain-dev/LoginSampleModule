package com.loginmodule.data.remote

import java.util.*

/**
 * Provide HashMap for request body.
 */
class CommonParams private constructor(builder: Builder) {
    val map: HashMap<String, String>

    class Builder {
        var map = HashMap<String, String>()

        fun add(key: String, value: Any?): Builder {
            if (value == null) return this
            map[key] = value.toString()
            return this
        }

        fun build(): CommonParams {
            return CommonParams(this)
        }
    }

    init {
        map = builder.map
    }
}