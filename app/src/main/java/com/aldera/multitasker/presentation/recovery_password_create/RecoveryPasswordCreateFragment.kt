package com.aldera.multitasker.presentation.recovery_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordCreateFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordCreateFragment : Fragment(R.layout.recovery_password_create_fragment) {
    private val binding by viewBinding(RecoveryPasswordCreateFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordCreateFragmentDirections.openLoginFragment()
            )
        }
        tvNoAccRegister.setOnClickListener {
            findNavController().navigate(
                RecoveryPasswordCreateFragmentDirections.openRegistrationFragment()
            )
        }
    }


}