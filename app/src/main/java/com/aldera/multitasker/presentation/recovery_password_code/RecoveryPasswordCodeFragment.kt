package com.aldera.multitasker.presentation.recovery_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordCodeFragmentBinding
import com.aldera.multitasker.presentation.recovery_password_email.RecoveryPasswordEmailFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordCodeFragment : Fragment(R.layout.recovery_password_code_fragment) {
    private val binding by viewBinding(RecoveryPasswordCodeFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordCodeFragmentDirections.openRecoveryPasswordCreateFragment()
            )
        }
        Navigation.createNavigateOnClickListener(R.id.recoveryPasswordCreateFragment)
        tvNoAccRegister.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordCodeFragmentDirections.openLoginFragment()
            )
        }

        tvNoAccRegister.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordEmailFragmentDirections.openRegistrationFragment()
            )
        }
    }

}
