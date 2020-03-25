package com.loginmodule.ui.launcher

import com.loginmodule.BR
import com.loginmodule.R
import com.loginmodule.databinding.ActivityLauncherBinding
import com.loginmodule.ui.base.BaseActivity
import com.loginmodule.ui.fragment.login.LoginFragment
import com.loginmodule.ui.fragment.splash.SplashFragment

class LauncherActivity : BaseActivity<ActivityLauncherBinding, LauncherViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_launcher
    }

    override fun init() {
        val userId = mViewModel.getUserId()

        if (userId.isNullOrEmpty()) {
            launchFragment(LoginFragment.newInstance())
        } else {
            launchFragment(SplashFragment.newInstance())
        }
    }

    override fun getViewModel(): Class<LauncherViewModel> {
        return LauncherViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }
}

