package com.aldera.multitasker.presentation.recovery.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RecoveryPasswordCreateFragmentBinding
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordCreateFragment : Fragment(R.layout.recovery_password_create_fragment) {
    private val binding by viewBinding(RecoveryPasswordCreateFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnContinue.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordCreateFragmentDirections.openLoginFragment()
            )
        }
        tvNoAccRegister.onClick {
            findNavController().navigateSafe(
                RecoveryPasswordCreateFragmentDirections.openRegistrationFragment()
            )
        }
    }
}
