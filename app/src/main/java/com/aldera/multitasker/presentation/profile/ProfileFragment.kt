package com.aldera.multitasker.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.ProfileFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.extension.showIf
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.profile_fragment) {
    private val binding by viewBinding(ProfileFragmentBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() = with(binding) {
        viewModel.loadData()
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
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
                handleSuccess(state)
            }
        }
    }

    private fun handleSuccess(state: UserViewState) = with(binding) {
        state.user?.let { user ->
            tvEmail.text = user.email
            tvName.showIf { !user.name.isNullOrEmpty() }
            if (user.avatar != null) {
                Glide.with(this@ProfileFragment).load(user.avatar).into(ivAvatar)
            }
        }
    }
}
