package com.loginmodule.ui.fragment.profile

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.loginmodule.R
import com.loginmodule.common.REQUEST_OPEN_CAMERA_ADD_IMAGE
import com.loginmodule.common.REQUEST_OPEN_GALLERY_ADD_IMAGE
import com.loginmodule.databinding.FragmentProfileBinding
import com.loginmodule.ui.base.BaseFragment
import com.loginmodule.ui.launcher.LauncherActivity
import com.loginmodule.util.ImageUtils
import com.loginmodule.util.datahelper.DataResponse
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

/**
 * The profile screen to show user data
 */

@RuntimePermissions
class ProfileFragment : BaseFragment<ProfileFragmentViewModel, FragmentProfileBinding>() {

    @Inject
    lateinit var imageUtils: ImageUtils

    public override fun getViewModel(): Class<ProfileFragmentViewModel> {
        return ProfileFragmentViewModel::class.java
    }

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
        dataBinding.ivAccountUserAvatar.setOnClickListener { changeImageWithPermissionCheck() }
        dataBinding.btnLogout.setOnClickListener { logout() }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        mViewModel.getUserId()?.let {
            mViewModel.getUser(it).observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    DataResponse.Status.LOADING -> {
                        dataBinding.progressLoader.visibility = View.VISIBLE
                    }
                    DataResponse.Status.SUCCESS -> {
                        dataBinding.progressLoader.visibility = View.GONE
                        dataBinding.userEntity = response.data
                        dataBinding.executePendingBindings()
                    }
                    DataResponse.Status.ERROR -> {
                        dataBinding.progressLoader.visibility = View.GONE
                    }
                }
            })
        }

    }

    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun changeImage() {
        imageUtils.showImageChooserDialog(activity)
    }

    @OnPermissionDenied(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    @OnNeverAskAgain(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun onCameraDenied() {
        Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_OPEN_CAMERA_ADD_IMAGE -> {
                compressAndSaveImageBitmap();
            }
            REQUEST_OPEN_GALLERY_ADD_IMAGE -> {
                imageUtils.copyFileFromUri(data?.data)
                compressAndSaveImageBitmap();
            }
        }
    }

    private fun compressAndSaveImageBitmap() {
        mViewModel.getUserImageUrl(imageUtils.compressImage())
            .observe(viewLifecycleOwner, Observer { response ->
                when (response.status) {
                    DataResponse.Status.LOADING -> {
                        dataBinding.progressLoader.visibility = View.VISIBLE
                    }
                    DataResponse.Status.SUCCESS -> {
                        response.data?.let { updateUserImage(it.avatarUrl) }
                        dataBinding.progressLoader.visibility = View.GONE
                    }
                    DataResponse.Status.ERROR -> {
                        dataBinding.progressLoader.visibility = View.GONE
                    }
                }
            })
    }

    private fun updateUserImage(avatarUrl: String) {
        mViewModel.getUserId()?.let { mViewModel.updateUserImage(it, avatarUrl) }
        dataBinding.userEntity?.avatarUrl = avatarUrl
        dataBinding.executePendingBindings()
    }

    private fun logout() {
        mViewModel.logoutUser()
        activity?.startActivity(Intent(activity, LauncherActivity::class.java))
        activity?.finishAffinity()
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment().apply {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutRes: Int
        get() = R.layout.fragment_profile
}
