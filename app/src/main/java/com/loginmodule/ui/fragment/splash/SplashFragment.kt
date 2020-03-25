package com.loginmodule.ui.fragment.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.loginmodule.R
import com.loginmodule.databinding.FragmentSplashBinding
import com.loginmodule.ui.base.BaseFragment
import com.loginmodule.ui.home.HomeActivity
import com.loginmodule.util.datahelper.DataResponse

/**
 * The splash fragment as launcher screen
 */
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    public override fun getViewModel(): Class<SplashViewModel> {
        return SplashViewModel::class.java
    }

    override val layoutRes: Int
        get() = R.layout.fragment_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()

    }

    private fun fetchData() {
        startActivity(Intent(activity, HomeActivity::class.java))
        activity?.finish()
    }

    companion object {
        fun newInstance(): SplashFragment = SplashFragment().apply {
            val args = Bundle()
            val fragment = SplashFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
