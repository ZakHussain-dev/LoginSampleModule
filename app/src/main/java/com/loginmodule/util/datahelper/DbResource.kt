package com.loginmodule.util.datahelper


import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class DbResource
@MainThread
protected constructor() {

    init {
        performOperation()
    }

    @MainThread
    private fun performOperation() {
        CoroutineScope(Dispatchers.IO).launch {
            readOrWriteDb()
        }
    }

    abstract suspend fun readOrWriteDb()
}
