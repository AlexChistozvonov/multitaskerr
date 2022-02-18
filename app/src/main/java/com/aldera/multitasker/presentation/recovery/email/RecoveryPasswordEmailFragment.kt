package com.aldera.multitasker.presentation.recovery.email

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordEmailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordEmailFragment : Fragment(R.layout.recovery_password_email_fragment) {
    private val binding by viewBinding(RecoveryPasswordEmailFragmentBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordEmailFragmentDirections.openRecoveryPasswordCodeFragment()
            )
        }
        tvNoAccRegister.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordEmailFragmentDirections.openLoginFragment()
            )
        }
    }
}
