package com.loginmodule.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.loginmodule.R
import com.loginmodule.databinding.FragmentLoginBinding
import com.loginmodule.ui.base.BaseFragment
import com.loginmodule.ui.home.HomeActivity
import com.loginmodule.util.datahelper.DataResponse
import com.loginmodule.util.isValidEmail

/**
 * The login screen for user to login the data
 */
class LoginFragment : BaseFragment<LoginFragmentViewModel, FragmentLoginBinding>() {
    public override fun getViewModel(): Class<LoginFragmentViewModel> {
        return LoginFragmentViewModel::class.java
    }

    override val layoutRes: Int
        get() = R.layout.fragment_login

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
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.btnLogin.setOnClickListener {
            onLogin()
        }
    }

    private fun onLogin() {
        if (isValid()) {
            mViewModel.loginUser(
                dataBinding.etEmail.text.toString(),
                dataBinding.etPassword.text.toString()
            ).observe(viewLifecycleOwner,
                Observer { response ->
                    when (response.status) {
                        DataResponse.Status.LOADING -> {
                            dataBinding.progressLoader.visibility = View.VISIBLE
                        }
                        DataResponse.Status.SUCCESS -> {
                            dataBinding.progressLoader.visibility = View.GONE
                            startActivity(Intent(activity, HomeActivity::class.java))
                            activity?.finish()
                        }
                        DataResponse.Status.ERROR -> {
                            dataBinding.progressLoader.visibility = View.GONE
                        }
                    }
                })
        }
    }

    private fun isValid(): Boolean {
        if (!dataBinding.etEmail.text.isValidEmail()) {
            dataBinding.etEmail.error = getString(R.string.invalid_email)
            return false
        }
        if (dataBinding.etPassword.text.isNullOrBlank()) {
            dataBinding.etPassword.error = getString(R.string.invalid_password)
            return false
        }
        return true
    }

    companion object {
        fun newInstance(): LoginFragment = LoginFragment().apply {
            val args = Bundle()
            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
