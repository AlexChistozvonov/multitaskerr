package com.aldera.multitasker.presentation.recovery.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordCodeFragmentBinding
import com.aldera.multitasker.presentation.recovery.email.RecoveryPasswordEmailFragmentDirections
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordCodeFragment : Fragment(R.layout.recovery_password_code_fragment) {
    private val binding by viewBinding(RecoveryPasswordCodeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordCodeFragmentDirections.openRecoveryPasswordCreateFragment()
            )
        }
        tvNoAccRegister.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordCodeFragmentDirections.openLoginFragment()
            )
        }

        tvNoAccRegister.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordEmailFragmentDirections.openRegistrationFragment()
            )
        }
    }
}
