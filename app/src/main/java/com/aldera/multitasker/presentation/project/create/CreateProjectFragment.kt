package com.aldera.multitasker.presentation.project.create

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.CreateProjectFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CreateProjectFragment : Fragment(R.layout.create_project_fragment) {
    private val binding by viewBinding(CreateProjectFragmentBinding::bind)
    private val viewModel by viewModels<CreateProjectViewModel>()
    private val args: CreateProjectFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        val id = args.category.id
        btnCreateProject.onClick {
            id?.let {
                viewModel.createProject(it)
            }
        }
        btnCancel.onClick { findNavController().popBackStack() }
        toolbar.apply {
            tvTitle.text = getString(R.string.create_project_)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: CreateProjectViewState) = with(binding) {
        fun showHide() {
            progressBar.hide()
            btnCreateProject.show()
            etEditName.show()
            btnCancel.show()
            tilName.show()
        }
        btnCreateProject.isEnabled = !etEditName.text.isNullOrEmpty()
        when (state.event) {
            is CreateProjectEvent.Error -> {
                showHide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            CreateProjectEvent.Init -> showHide()
            CreateProjectEvent.Loading -> {
                progressBar.show()
                btnCreateProject.hide()
                etEditName.hide()
                btnCancel.hide()
                tilName.hide()
            }
            CreateProjectEvent.Success -> {
                findNavController().popBackStack()
                showHide()
            }
            is CreateProjectEvent.TitleChanged -> showHide()
        }
    }
}
