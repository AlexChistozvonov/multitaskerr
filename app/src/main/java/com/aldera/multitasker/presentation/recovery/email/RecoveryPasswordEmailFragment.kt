package com.aldera.multitasker.presentation.recovery.email

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
import com.aldera.multitasker.databinding.RecoveryPasswordEmailFragmentBinding
import com.aldera.multitasker.presentation.login.LoginFragmentDirections
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RecoveryPasswordEmailFragment : Fragment(R.layout.recovery_password_email_fragment) {
    private val binding by viewBinding(RecoveryPasswordEmailFragmentBinding::bind)
    private val viewModel by viewModels<RecoveryPasswordEmailViewModel>()

    private val clickableSpanNoAcc = object : ClickableSpan() {
        override fun onClick(p0: View) {
            findNavController().navigateSafe(
                LoginFragmentDirections.openRegistrationFragment()
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
        viewModel.navigationEvent.onEach {
            handleNavigation(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleNavigation(it: Event<RecoveryPasswordEmailNavigationEvent>) {
        when (val data = it.content()) {
            is RecoveryPasswordEmailNavigationEvent.NextStep -> findNavController().navigateSafe(
                RecoveryPasswordEmailFragmentDirections.openRecoveryPasswordCodeFragment(data.key)
            )
            null -> {}
        }
    }

    private fun handleState(state: RecoveryPasswordEmailViewState) = with(binding) {
        btnContinue.isEnabled = !etEmail.text.isNullOrEmpty()
        when (state.event) {
            is RecoveryPasswordEmailEvent.EmailChanged -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.error = null
            }
            RecoveryPasswordEmailEvent.EmailError -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.error =
                    getString(R.string.error_email)
            }
            is RecoveryPasswordEmailEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            RecoveryPasswordEmailEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
            }
            RecoveryPasswordEmailEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
            }
            is RecoveryPasswordEmailEvent.Key -> {}
        }
    }

    private fun initView() = with(binding) {
        createSpannable()
        etEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        btnContinue.onClick { viewModel.recoveryPasswordEmail() }
        ibClose.onClick { findNavController().popBackStack() }
    }

    private fun createSpannable() = with(binding) {
        val noAccString = getString(R.string.login_no_acc_register)
        val spannedNoAcc = SpannableStringBuilder(noAccString)
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
            tvNoAccRegister.movementMethod = LinkMovementMethod.getInstance()
            tvNoAccRegister.text = spannedNoAcc
        }
    }
}
