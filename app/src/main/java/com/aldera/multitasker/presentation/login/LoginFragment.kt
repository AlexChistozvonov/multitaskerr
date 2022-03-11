package com.aldera.multitasker.presentation.login

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.LoginFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding by viewBinding(LoginFragmentBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    private val clickableSpanNoAcc = object : ClickableSpan() {
        override fun onClick(p0: View) {
            findNavController().navigateSafe(
                LoginFragmentDirections.openRegistrationFragment()
            )
        }
    }
    private val clickableSpanForgotPassword = object : ClickableSpan() {
        override fun onClick(p0: View) {
            findNavController().navigateSafe(
                LoginFragmentDirections.openRecoveryPasswordEmailFragment()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: LoginViewState) = with(binding) {
        btnInput.isEnabled = !etEmail.text.isNullOrEmpty() && !etPassword.text.isNullOrEmpty()
        when (state.event) {
            is LoginEvent.EmailChanged -> {
                progressBar.show()
                btnInput.show()
                tilEmail.error = null
            }
            LoginEvent.Loading -> {
                progressBar.show()
                btnInput.show()
            }
            is LoginEvent.PasswordChanged -> {
                progressBar.hide()
                btnInput.show()
                tilPassword.error = null
            }
            is LoginEvent.PasswordError -> {
                progressBar.hide()
                btnInput.show()
                tilPassword.error = getString(R.string.error_password)
            }
            is LoginEvent.EmailError -> {
                progressBar.hide()
                btnInput.show()
                tilEmail.error = getString(R.string.error_email)
            }
            is LoginEvent.Error -> {
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
                progressBar.hide()
                binding.btnInput.show()
            }
            LoginEvent.Success -> {
                progressBar.hide()
                btnInput.show()
                findNavController().navigateSafe(
                    LoginFragmentDirections.openProfileFragment()
                )
            }
            LoginEvent.Init -> {
                progressBar.hide()
                btnInput.show()
            }
        }
    }

    private fun initView() = with(binding) {
        createSpannable()
        etEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        etPassword.doOnTextChanged { text, _, _, _ -> viewModel.onPasswordTextChanged(text.toString()) }
        ibClose.onClick { findNavController().popBackStack() }
        btnInput.onClick { viewModel.login() }
    }

    private fun createSpannable() = with(binding) {
        val forgotPassword = getString(R.string.login_forgot_password)
        val spannableForgotPassword = SpannableStringBuilder(forgotPassword)
        val noAccString = getString(R.string.login_no_acc_register)
        val spannedNoAcc = SpannableStringBuilder(noAccString)
        spannableForgotPassword.apply {
            setSpan(
                clickableSpanForgotPassword,
                0,
                forgotPassword.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                forgotPassword.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        spannedNoAcc.apply {
            setSpan(
                clickableSpanNoAcc,
                noAccString.lastIndexOf(' ') + 1,
                noAccString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                noAccString.lastIndexOf(' ') + 1,
                noAccString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        tvForgotPassword.movementMethod = LinkMovementMethod.getInstance()
        tvForgotPassword.text = spannableForgotPassword
        tvNoAccRegister.movementMethod = LinkMovementMethod.getInstance()
        tvNoAccRegister.text = spannedNoAcc
    }
}
