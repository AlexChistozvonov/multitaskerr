package com.aldera.multitasker.presentation.recovery.code

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
import com.aldera.multitasker.databinding.RecoveryPasswordCodeFragmentBinding
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
class RecoveryPasswordCodeFragment : Fragment(R.layout.recovery_password_code_fragment) {
    private val binding by viewBinding(RecoveryPasswordCodeFragmentBinding::bind)
    private val viewModel by viewModels<RecoveryPasswordCodeViewModel>()
    private val args: RecoveryPasswordCodeFragmentArgs by navArgs()

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
        viewModel.navigationEvent.onEach { openCreateFragment(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun openCreateFragment(it: Event<RecoveryPasswordCodeNavigationEvent>) {
        when (val data = it.content()) {
            is RecoveryPasswordCodeNavigationEvent.NextStep -> findNavController().navigateSafe(
                RecoveryPasswordCodeFragmentDirections.openRecoveryPasswordCreateFragment(data.key)
            )
            else -> {}
        }
    }

    private fun handleState(state: RecoveryPasswordCodeViewState) = with(binding) {
        btnContinue.isEnabled = !etInputCode.text.isNullOrEmpty()
        when (state.event) {
            is RecoveryPasswordCodeEvent.CodeChange -> {
                progressBar.hide()
                btnContinue.show()
                tilCode.error = null
            }
            RecoveryPasswordCodeEvent.CodeError -> {
                progressBar.hide()
                btnContinue.show()
                tilCode.error =
                    getString(R.string.error_code)
            }
            is RecoveryPasswordCodeEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            RecoveryPasswordCodeEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
            }
            RecoveryPasswordCodeEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
            }
            else -> {}
        }
    }

    private fun initView() = with(binding) {
        createSpannable()
        etInputCode.doOnTextChanged { text, _, _, _ -> viewModel.onCodeTextChanged(text.toString()) }
        btnContinue.onClick {
            if (binding.etInputCode.text.toString().length != LENGTH_CODE) {
                binding.tilCode.error = getString(R.string.error_code)
            } else {
                args.keyScreenCode?.let { viewModel.recoveryPasswordCode(it) }
            }
        }
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

    companion object {
        const val LENGTH_CODE = 6
    }
}
