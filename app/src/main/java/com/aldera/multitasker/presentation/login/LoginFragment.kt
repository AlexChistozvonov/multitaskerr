package com.aldera.multitasker.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.LoginFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding by viewBinding(LoginFragmentBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: LoginViewState) {
        when (state.event) {
            is LoginEvent.EmailChanged -> {
                binding.tilEmail.error = null
            }
            LoginEvent.Loading -> {}
            is LoginEvent.PasswordChanged -> {
                binding.tilPassword.error = null
            }
            is LoginEvent.PasswordError -> {
                binding.tilPassword.error = getString(R.string.error_password)
            }
            is LoginEvent.EmailError -> {
                binding.tilEmail.error = getString(R.string.error_email)
            }
        }
    }

    private fun initView() = with(binding) {
        tvNoAccRegister.setOnClickListener { findNavController().navigate(LoginFragmentDirections.openRegistrationFragment()) }
        tvForgotPassword.setOnClickListener { findNavController().navigate(LoginFragmentDirections.openRecoveryPasswordEmailFragment()) }

        etEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        etPassword.doOnTextChanged { text, _, _, _ -> viewModel.onPasswordTextChanged(text.toString()) }
        btnInput.setOnClickListener { viewModel.login() }
        ibClose.setOnClickListener { findNavController().popBackStack() }
    }
}
