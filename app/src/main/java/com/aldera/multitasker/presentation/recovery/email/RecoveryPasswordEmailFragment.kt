package com.aldera.multitasker.presentation.recovery.email

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordEmailFragmentBinding
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordEmailFragment : Fragment(R.layout.recovery_password_email_fragment) {
    private val binding by viewBinding(RecoveryPasswordEmailFragmentBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordEmailFragmentDirections.openRecoveryPasswordCodeFragment()
            )
        }
        tvNoAccRegister.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordEmailFragmentDirections.openLoginFragment()
            )
        }
    }
}
