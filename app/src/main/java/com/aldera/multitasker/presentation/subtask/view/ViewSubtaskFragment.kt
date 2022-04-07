package com.aldera.multitasker.presentation.subtask.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.ViewSubtaskFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ViewSubtaskFragment : Fragment(R.layout.view_subtask_fragment) {
    private val binding by viewBinding(ViewSubtaskFragmentBinding::bind)
    private val args: ViewSubtaskFragmentArgs by navArgs()
    private val viewModel by viewModels<ViewSubtaskViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() = with(binding) {
        val id = args.task?.id
        id?.let {
            viewModel.getViewSubtask(it)
        }
        toolbar.apply {
            tvTitle.text = getString(R.string.subtask)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
        tvNameTask.text = args.createSubtask.title
        tvTitleTask.text = args.category.title
        llNameCategory.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(args.category.color))
        if (etDescription.text.isNullOrEmpty()) {
            etDescription.hide()
            tvDescriptionTask.hide()
        } else {
            etDescription.text = args.createSubtask.description
        }
        etPeriodOfExecutionTask.text = args.createSubtask.deadline
        Glide.with(requireContext()).load(args.createSubtask.performer?.avatar)
            .into(ivAvatarExecutor)
        tvNameExecutorSubtask.text = args.createSubtask.performer?.name
        initRgGroup()
    }

    private fun initRgGroup() = with(binding) {
        when (args.createSubtask.importance) {
            Constants.IMPORTANCE_1 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_1).into(ivImportance)
            }
            Constants.IMPORTANCE_2 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_2).into(ivImportance)
            }
            Constants.IMPORTANCE_3 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_3).into(ivImportance)
            }
            Constants.IMPORTANCE_4 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_4).into(ivImportance)
            }
            else -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_4).into(ivImportance)
            }
        }
    }

    private fun handleSuccess(state: ViewSubtaskViewState) = with(binding) {
        state.subTask?.let { subTask ->
            tvNameAuthor.text = subTask.author?.name
            Glide.with(requireContext()).load(subTask.author?.avatar).into(ivAvatarAuthor)
            tvCreated.text = getString(R.string.created, subTask.createdAt)
            if (subTask.updatedAt != null) {
                tvUpdated.text = getString(R.string.updated, subTask.updatedAt)
            } else {
                tvUpdated.hide()
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: ViewSubtaskViewState) = with(binding) {
        when (state.event) {
            is ViewSubtaskEvent.Error -> {
                nestedScrollView.show()
                progressBar.hide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            ViewSubtaskEvent.Init -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            ViewSubtaskEvent.Loading -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            is ViewSubtaskEvent.Success -> {
                handleSuccess(state)
                nestedScrollView.hide()
                progressBar.show()
            }
        }
    }
}
