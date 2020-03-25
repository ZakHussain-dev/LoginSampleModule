package com.loginmodule.di.builder

import com.loginmodule.ui.fragment.login.LoginFragment
import com.loginmodule.ui.fragment.profile.ProfileFragment
import com.loginmodule.ui.fragment.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Providing support for injecting module with android components
 */
@Module
abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun contributeProfileFragment(): ProfileFragment
}
