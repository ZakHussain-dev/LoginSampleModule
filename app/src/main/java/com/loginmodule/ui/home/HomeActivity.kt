package com.loginmodule.ui.home

import android.content.Intent
import com.loginmodule.BR
import com.loginmodule.R
import com.loginmodule.databinding.ActivityHomeBinding
import com.loginmodule.ui.base.BaseActivity
import com.loginmodule.ui.fragment.profile.ProfileFragment

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_home
    }

    override fun init() {
        launchFragment(ProfileFragment.newInstance())
    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getProfileFragment()?.onActivityResult(requestCode, resultCode, data)
    }

    private fun getProfileFragment(): ProfileFragment? {
        val fragment =
            supportFragmentManager.findFragmentByTag(ProfileFragment::class.java.canonicalName)
        if (fragment is ProfileFragment) {
            return fragment
        }
        return null
    }
}

