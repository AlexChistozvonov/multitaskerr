package com.aldera.multitasker.presentation.project.edit

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
import com.aldera.multitasker.databinding.EditProjectFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditProjectFragment : Fragment(R.layout.edit_project_fragment) {
    private val binding by viewBinding(EditProjectFragmentBinding::bind)
    private val viewModel by viewModels<EditProjectViewModel>()
    private val args: EditProjectFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        val title = args.project.title
        val id = args.project.id
        val categoryId = args.category.id
        btnSave.onClick {
            id?.let { id ->
                categoryId?.let { categoryId ->
                    viewModel.editProject(
                        id,
                        categoryId
                    )
                }
            }
        }
        toolbar.apply {
            tvTitle.text = getString(R.string._edit)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
        etEditName.setText(title)
        btnDelete.onClick {
            id?.let {
                viewModel.deleteProject(it)
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: EditProjectViewState) = with(binding) {
        fun showHide() {
            progressBar.hide()
            btnSave.show()
            etEditName.show()
            tilName.show()
        }
        btnSave.isEnabled = !etEditName.text.isNullOrEmpty()
        when (state.event) {
            is EditProjectEvent.Error -> {
                showHide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            EditProjectEvent.Init -> showHide()
            EditProjectEvent.Loading -> {
                progressBar.show()
                btnSave.hide()
                etEditName.hide()
                tilName.hide()
            }
            EditProjectEvent.Success -> {
                findNavController().popBackStack()
                showHide()
            }
            is EditProjectEvent.TitleChanged -> showHide()
            is EditProjectEvent.ExitProfileError -> showHide()
        }
    }
}
