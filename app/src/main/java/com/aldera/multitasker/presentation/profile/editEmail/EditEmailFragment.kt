package com.aldera.multitasker.presentation.profile.editEmail

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.EditEmailFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditEmailFragment : Fragment(R.layout.edit_email_fragment) {
    private val binding by viewBinding(EditEmailFragmentBinding::bind)
    private val viewModel by viewModels<EditEmailViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        etEditEmail.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        btnContinue.onClick {
            viewModel.editEmail()
        }
        toolbar.apply {
            tvTitle.text = getString(R.string.edit_email)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: EditEmailViewState) = with(binding) {
        when (state.event) {
            is EditEmailEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.show()
                tvEmail.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            EditEmailEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
                tilEmail.hide()
                tvEmail.hide()
            }
            is EditEmailEvent.EmailChanged -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.show()
                tvEmail.show()
                tilEmail.error = null
            }
            EditEmailEvent.EmailError -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.show()
                tvEmail.show()
                tilEmail.error = getString(R.string.error_name)
            }
            is EditEmailEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.show()
                tvEmail.show()
                findNavController().popBackStack()
            }
            EditEmailEvent.Init -> {
                progressBar.hide()
                btnContinue.show()
                tilEmail.show()
                tvEmail.show()
            }
        }
    }
}
