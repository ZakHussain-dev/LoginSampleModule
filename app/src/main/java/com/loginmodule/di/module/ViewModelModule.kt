package com.loginmodule.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.loginmodule.ViewModelFactory
import com.loginmodule.di.ViewModelKey
import com.loginmodule.ui.fragment.login.LoginFragmentViewModel
import com.loginmodule.ui.fragment.profile.ProfileFragmentViewModel
import com.loginmodule.ui.fragment.splash.SplashViewModel
import com.loginmodule.ui.home.HomeViewModel
import com.loginmodule.ui.launcher.LauncherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Module for injecting viewmodel with dagger multibindings
 *
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LauncherViewModel::class)
    abstract fun bindLauncherActivityViewModel(launcherViewModel: LauncherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeActivityViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    abstract fun bindLoginFragmentViewModel(loginFragmentViewModel: LoginFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileFragmentViewModel::class)
    abstract fun bindProfileFragmentViewModel(profileFragmentViewModel: ProfileFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashFragmentViewModel(userListViewModel: SplashViewModel): ViewModel

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}