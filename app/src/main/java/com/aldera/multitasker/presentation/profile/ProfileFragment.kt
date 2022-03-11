package com.aldera.multitasker.presentation.profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.BuildConfig
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.imageUrl
import com.aldera.multitasker.databinding.ProfileFragmentBinding
import com.aldera.multitasker.presentation.ImagePickerDialogFragment
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private val binding by viewBinding(ProfileFragmentBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() = with(binding) {
        viewModel.loadData()
        llEditName.onClick {
            findNavController().navigateSafe(
                ProfileFragmentDirections.openEditNameFragment(
                    viewModel.uiState.value.user
                )
            )
        }
        llEditEmail.onClick {
            findNavController().navigateSafe(
                ProfileFragmentDirections.openEditEmailFragment(
                    viewModel.uiState.value.user
                )
            )
        }
        btnEditAvatar.onClick {
            findNavController().navigateSafe(ProfileFragmentDirections.openImagePickerDialog())
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        setFragmentResultListener(ImagePickerDialogFragment.CAMERA) { _, _ -> takeImage() }
        setFragmentResultListener(ImagePickerDialogFragment.GALLERY) { _, _ -> selectImageFromGallery() }
        setFragmentResultListener(ImagePickerDialogFragment.DELETE) { _, _ -> deleteImage() }
    }

    private fun handleState(state: UserViewState) = with(binding) {
        when (state.event) {
            is UserEvent.Error -> {
                progressBar.hide()
                linearLayout.show()
                btnGoOut.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            UserEvent.Loading -> {
                progressBar.show()
                linearLayout.hide()
                btnGoOut.hide()
            }
            is UserEvent.Success -> {
                progressBar.hide()
                linearLayout.show()
                btnGoOut.show()
                progressBarImage.hide()
                handleSuccess(state)
            }
            is UserEvent.ImageError -> {
                progressBar.hide()
                linearLayout.show()
                btnGoOut.show()
            }
            UserEvent.ImageLoading -> {
                progressBarImage.show()
            }
        }
    }

    private fun handleSuccess(state: UserViewState) = with(binding) {
        state.user?.let { user ->
            tvEmail.text = user.email
            if (user.name != null) {
                tvName.text = user.name
            }

            if (user.avatar != null) {
                Glide.with(requireContext()).load(user.avatar!!.imageUrl())
                    .fallback(R.drawable.ic_avatar).circleCrop().into(ivAvatar)
            } else {
                Glide.with(requireContext()).load(user.avatar)
                    .fallback(R.drawable.ic_avatar).circleCrop().into(ivAvatar)
            }
        }
    }

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isSuccess ->
            if (isSuccess) {
                takeImageResult.launch(latestTmpUri)
            }
        }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.preloadImage(latestTmpUri)
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { uri ->
                viewModel.preloadImage(uri)
            }
        }

    private fun deleteImage() {
        viewModel.deleteAvatar()
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                permissions.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch(IMAGE_MIME_TYPE)

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile(
            IMAGE_FILE_PREFIX,
            SUFFIX,
            requireContext().cacheDir
        ).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            AUTHORITY,
            tmpFile
        )
    }

    companion object {
        private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
        private const val IMAGE_MIME_TYPE = "image/*"
        private const val SUFFIX = ".png"
        private const val IMAGE_FILE_PREFIX = "tmp_image_file"
    }
}
