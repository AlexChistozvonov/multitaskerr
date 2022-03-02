package com.aldera.multitasker.presentation.recovery.create

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
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordCreateFragmentBinding
import com.aldera.multitasker.presentation.login.LoginFragmentDirections
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RecoveryPasswordCreateFragment : Fragment(R.layout.recovery_password_create_fragment) {
    private val binding by viewBinding(RecoveryPasswordCreateFragmentBinding::bind)
    private val viewModel by viewModels<RecoveryPasswordCreateViewModel>()
    private val args: RecoveryPasswordCreateFragmentArgs by navArgs()

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

    private fun initView() = with(binding) {
        createSpannable()
        etNewPassword.doOnTextChanged { text, _, _, _ -> viewModel.onPasswordTextChanged(text.toString()) }
        etRepeatNewPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordTextChanged(
                text.toString(),
                true
            )
        }
        btnContinue.onClick {
            val password = binding.etNewPassword.text.toString()
            val password2 = binding.etRepeatNewPassword.text.toString()
            if (password == password2) {
                args.keyScreenCreate?.let {
                    viewModel.recoveryPasswordCreate(it)
                    findNavController().navigateSafe(
                        RecoveryPasswordCreateFragmentDirections.openLoginFragment()
                    )
                }
            } else {
                binding.tilPasswordRepeat.error = getString(R.string.different_passwords)
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.navigationEvent
    }

    private fun handleState(state: RecoveryPasswordCreateViewState) = with(binding) {
        btnContinue.isEnabled =
            !etNewPassword.text.isNullOrEmpty() && !etRepeatNewPassword.text.isNullOrEmpty()
        when (state.event) {
            is RecoveryPasswordCreateEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            RecoveryPasswordCreateEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
            }
            is RecoveryPasswordCreateEvent.Password2Changed -> {
                tilPasswordRepeat.error = null
                progressBar.hide()
                btnContinue.show()
            }
            is RecoveryPasswordCreateEvent.PasswordChanged -> {
                tilNewPassword.error = null
                progressBar.hide()
                btnContinue.show()
            }
            RecoveryPasswordCreateEvent.PasswordError -> {
                progressBar.hide()
                btnContinue.show()
                tilNewPassword.error = getString(R.string.error_password)
            }
            RecoveryPasswordCreateEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
            }
        }
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
