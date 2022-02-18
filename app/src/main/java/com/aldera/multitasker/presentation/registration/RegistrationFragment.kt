package com.aldera.multitasker.presentation.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.RegistrationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.registration_fragment) {
    private val binding by viewBinding(RegistrationFragmentBinding::bind)
    private val viewModel by viewModels<RegistrationViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        tvHaveRegister.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.openLoginFragment()
            )
        }

        ibClose.setOnClickListener { findNavController().popBackStack() }
        btnRegister.setOnClickListener { viewModel.registration() }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: RegistrationViewState) {
        when (state.event) {
            is RegistrationEvent.EmailChanged -> {}
            RegistrationEvent.Loading -> {}
            is RegistrationEvent.PasswordChanged -> {}
            RegistrationEvent.EmailError -> {
            }
            else -> {}
        }
    }
}
