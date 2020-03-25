package com.loginmodule

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import com.loginmodule.common.isRelease
import com.loginmodule.di.component.DaggerAppComponent
import com.loginmodule.util.Prefs
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * Custom Application class for initializing components at application level
 */
class BaseApplication : DaggerApplication() {

    @Inject
    lateinit var activityDispatchingInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        if (!isRelease()) {
            Timber.uprootAll()
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<BaseApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}