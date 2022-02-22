package com.aldera.multitasker.presentation.registration

import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import com.aldera.multitasker.BuildConfig
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RegistrationFragmentBinding
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.ConstantApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.registration_fragment) {
    private val binding by viewBinding(RegistrationFragmentBinding::bind)
    private val viewModel by viewModels<RegistrationViewModel>()
    private val clickableSpanOpenFragment = object : ClickableSpan() {
        override fun onClick(p0: View) {
            findNavController().navigateSafe(
                RegistrationFragmentDirections.openLoginFragment()
            )
        }
    }

    private val clickableSpanOpenUrlAttachment = object : ClickableSpan() {
        override fun onClick(p0: View) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(BuildConfig.SERVER_URL + ConstantApi.terms_of_use)
            startActivity(openURL)
        }
    }

    private val clickableSpanOpenUrlPrivacy = object : ClickableSpan() {
        override fun onClick(p0: View) {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(BuildConfig.SERVER_URL + ConstantApi.privacy_police)
            startActivity(openURL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        createSpannable()
        ibClose.onClick { findNavController().popBackStack() }
        etEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        etPassword.doOnTextChanged { text, _, _, _ -> viewModel.onPasswordTextChanged(text.toString()) }
        etRepeatPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordTextChanged(
                text.toString(),
                true
            )
        }
        btnRegister.onClick {
            val password = binding.etPassword.text.toString()
            val password2 = binding.etRepeatPassword.text.toString()
            if (password == password2) {
                viewModel.registration()
            } else {
                binding.tilPasswordRepeat.error = getString(R.string.different_passwords)
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: RegistrationViewState) {
        when (state.event) {
            is RegistrationEvent.EmailChanged -> {
                binding.tilEmail.error = null
            }
            RegistrationEvent.Loading -> {}
            is RegistrationEvent.PasswordChanged -> {
                binding.tilPassword.error = null
                binding.tilPasswordRepeat.error = null
            }
            is RegistrationEvent.PasswordError -> {
                binding.tilPassword.error = getString(R.string.error_password)
            }
            is RegistrationEvent.EmailError -> {
                binding.tilEmail.error = getString(R.string.error_email)
            }
            is RegistrationEvent.Password2Changed -> {
                binding.tilPasswordRepeat.error = null
            }
            is RegistrationEvent.Error -> {
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
        }
    }

    private fun createSpannable() = with(binding) {
        val agreementString = getString(R.string.registration_agreement)
        val spannableAgreement = SpannableStringBuilder(agreementString)

        spannableAgreement.apply {
            setSpan(
                clickableSpanOpenUrlPrivacy,
                PRIVACY_START_INDEX,
                PRIVACY_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                PRIVACY_START_INDEX,
                PRIVACY_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                clickableSpanOpenUrlAttachment,
                ATTACHMENT_START_INDEX,
                ATTACHMENT_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                ATTACHMENT_START_INDEX,
                ATTACHMENT_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        val rawString = getString(R.string.registration_have_acc)
        val spannableHaveAcc = SpannableStringBuilder(rawString)
        spannableHaveAcc.apply {
            val startIndex = rawString.lastIndexOf(' ') + 1
            setSpan(
                clickableSpanOpenFragment,
                startIndex,
                rawString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(Color.BLACK),
                startIndex,
                rawString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        tvHaveRegister.movementMethod = LinkMovementMethod.getInstance()
        tvHaveRegister.text = spannableHaveAcc
        tvAgreement.movementMethod = LinkMovementMethod.getInstance()
        tvAgreement.text = spannableAgreement
    }

    companion object {
        const val PRIVACY_START_INDEX = 62
        const val PRIVACY_END_INDEX = 90
        const val ATTACHMENT_START_INDEX = 94
        const val ATTACHMENT_END_INDEX = 128
    }
}
