package com.aldera.multitasker.presentation.profile.editName

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.EditNameFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditNameFragment : Fragment(R.layout.edit_name_fragment) {
    private val binding by viewBinding(EditNameFragmentBinding::bind)
    private val viewModel by viewModels<EditNameViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onEmailTextChanged(text.toString()) }
        btnContinue.onClick {
            viewModel.editName()
        }
        toolbar.apply {
            tvTitle.text = getString(R.string.edit_name)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: EditNameViewState) = with(binding) {
        btnContinue.isEnabled = !etEditName.text.isNullOrEmpty()
        when (state.event) {
            is EditNameEvent.Error -> {
                progressBar.hide()
                btnContinue.show()
                tilName.show()
                tvName.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            EditNameEvent.Loading -> {
                progressBar.show()
                btnContinue.hide()
                tilName.hide()
                tvName.hide()
            }
            is EditNameEvent.NameChanged -> {
                progressBar.hide()
                btnContinue.show()
                tilName.show()
                tvName.show()
                tilName.error = null
            }
            EditNameEvent.NameError -> {
                progressBar.hide()
                btnContinue.show()
                tilName.show()
                tvName.show()
                tilName.error = getString(R.string.error_name)
            }
            is EditNameEvent.Success -> {
                progressBar.hide()
                btnContinue.show()
                tilName.show()
                tvName.show()
                findNavController().popBackStack()
            }
            EditNameEvent.Init -> {
                progressBar.hide()
                btnContinue.show()
                tilName.show()
                tvName.show()
            }
        }
    }
}
