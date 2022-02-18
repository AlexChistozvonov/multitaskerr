package com.aldera.multitasker.presentation.launch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.LaunchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchFragment : Fragment(R.layout.launch_fragment) {
    private val binding by viewBinding(LaunchFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        btnEnter.setOnClickListener { findNavController().navigate(LaunchFragmentDirections.openLoginFragment()) }
        btnRegistration.setOnClickListener { findNavController().navigate(LaunchFragmentDirections.openRegistrationFragment()) }
    }
}