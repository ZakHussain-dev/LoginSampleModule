package com.loginmodule.di.builder

import com.loginmodule.ui.home.HomeActivity
import com.loginmodule.ui.launcher.LauncherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Providing support for injecting module with android components
 */
@Module(includes = [FragmentBuilderModule::class])
internal abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun launcherActivity(): LauncherActivity

}
