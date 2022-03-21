package com.aldera.multitasker.presentation.profile.editPassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.EditPasswordFragmentBinding
import com.aldera.multitasker.presentation.editPassword.EditPasswordEvent
import com.aldera.multitasker.presentation.editPassword.EditPasswordViewModel
import com.aldera.multitasker.presentation.editPassword.EditPasswordViewState
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditPasswordFragment : Fragment(R.layout.edit_password_fragment) {
    private val binding by viewBinding(EditPasswordFragmentBinding::bind)
    private val viewModel by viewModels<EditPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        toolbar.apply {
            tvTitle.text = getString(R.string.edit_password_)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
        etOldPassword.doOnTextChanged { text, _, _, _ -> viewModel.onOldPasswordTextChanged(text.toString()) }
        etNewPassword.doOnTextChanged { text, _, _, _ -> viewModel.onNewPasswordTextChanged(text.toString()) }
        btnContinue.onClick {
            val password = binding.etNewPassword.text.toString()
            val password2 = binding.etRepeatNewPassword.text.toString()
            if (password == password2) {
                viewModel.editPassword()
            } else {
                tilRepeatNewPassword.error = getString(R.string.different_passwords)
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handlerState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handlerState(state: EditPasswordViewState) = with(binding) {
        btnContinue.isEnabled =
            !etNewPassword.text.isNullOrEmpty() && !etOldPassword.text.isNullOrEmpty() && !etRepeatNewPassword.text.isNullOrEmpty()
        when (state.event) {
            is EditPasswordEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            EditPasswordEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
            }
            is EditPasswordEvent.NewPasswordChanged -> {
                progressBar.hide()
                btnContinue.show()
                tilNewPassword.error = null
                tilOldPassword.error = null
                tilRepeatNewPassword.error = null
            }
            EditPasswordEvent.NewPasswordError -> {
                progressBar.hide()
                btnContinue.show()
                tilNewPassword.error = getString(R.string.error_new_password)
            }
            is EditPasswordEvent.OldPasswordChanged -> {
                progressBar.hide()
                btnContinue.show()
                tilNewPassword.error = null
                tilOldPassword.error = null
                tilRepeatNewPassword.error = null
            }
            EditPasswordEvent.OldPasswordError -> {
                progressBar.hide()
                btnContinue.show()
                tilOldPassword.error = getString(R.string.error_old_password)
            }
            EditPasswordEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
                findNavController().popBackStack()
            }
            EditPasswordEvent.Init -> {
                progressBar.hide()
                btnContinue.show()
            }
        }
    }
}
