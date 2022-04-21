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
import com.aldera.multitasker.data.models.imageUrl
import com.aldera.multitasker.databinding.ViewSubtaskFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
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
        val id = args.subTaskResponse?.id
        id?.let {
            viewModel.getViewSubtask(it)
        }
        btnDelete.onClick {
            id?.let {
                viewModel.deleteSubtask(it)
            }
        }
        toolbar.apply {
            tvTitle.text = getString(R.string.subtask)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }

        Glide.with(requireContext()).load(args.task?.performer?.avatar?.imageUrl()).circleCrop()
            .into(ivAvatar)
        if (args.task?.performer?.name == null) {
            tvExecutorTask.text = args.task?.performer?.email
        } else {
            tvExecutorTask.text = args.task?.performer?.name
        }
        tvNameTask.text = args.task?.title
        tvTitleCategory.text = args.category.title
        tvTitleTask.text = args.category.title
        llNameCategory.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(args.category.color))
        llNameCategoryTask.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(args.category.color))

        when (args.task?.importance) {
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
        btnEditTask.onClick {
            findNavController().navigateSafe(
                ViewSubtaskFragmentDirections.openEditSubtaskFragment(
                    state.subTask,
                    args.category
                )
            )
        }
        tvNameSubtask.text = state.subTask?.title
        if (state.subTask?.description.isNullOrEmpty()) {
            etDescription.hide()
            tvDescriptionTask.hide()
        } else {
            etDescription.text = state.subTask?.description
        }

        val parser = SimpleDateFormat(Constants.DATE_FORMAT_PARSER, Locale(Constants.DATE_LOCALE))
        val formatter = SimpleDateFormat(Constants.DATE_FORMAT_MMMM, Locale(Constants.DATE_LOCALE))
        val formatterTime =
            SimpleDateFormat(Constants.DATE_FORMAT_HH, Locale(Constants.DATE_LOCALE))
        val outputDeadline = formatter.format(parser.parse(state.subTask?.deadline))
        val outputCreated = formatterTime.format(parser.parse(state.subTask?.createdAt))
        etPeriodOfExecutionTask.text = outputDeadline.toString()

        state.subTask?.let { subtask ->
            tvNameAuthor.text = subtask.author?.name
            Glide.with(requireContext()).load(subtask.author?.avatar?.imageUrl()).circleCrop()
                .into(ivAvatarAuthor)
            tvCreated.text = getString(R.string.created, outputCreated)
            if (subtask.updatedAt == null) {
                tvUpdated.hide()
            } else {
                val outputUpdated = formatterTime.format(parser.parse(state.subTask.updatedAt))
                tvUpdated.text = getString(R.string.updated, outputUpdated)
            }
        }

        Glide.with(requireContext()).load(state.subTask?.performer?.avatar?.imageUrl()).circleCrop()
            .into(ivAvatarExecutor)
        if (state.subTask?.performer?.name == null) {
            tvNameExecutorSubtask.text = state.subTask?.performer?.email
        } else {
            tvNameExecutorSubtask.text = state.subTask.performer.name
        }
        initImportance(state)
    }

    private fun initImportance(state: ViewSubtaskViewState) = with(binding) {
        when (state.subTask?.importance) {
            Constants.IMPORTANCE_1 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_1)
                    .into(ivImportanceSubtask)
            }
            Constants.IMPORTANCE_2 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_2)
                    .into(ivImportanceSubtask)
            }
            Constants.IMPORTANCE_3 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_3)
                    .into(ivImportanceSubtask)
            }
            Constants.IMPORTANCE_4 -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_4)
                    .into(ivImportanceSubtask)
            }
            else -> {
                Glide.with(requireContext()).load(R.drawable.ic_urgently_4)
                    .into(ivImportanceSubtask)
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
                nestedScrollView.show()
                progressBar.hide()
            }
            ViewSubtaskEvent.Loading -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            is ViewSubtaskEvent.Success -> {
                handleSuccess(state)
                nestedScrollView.show()
                progressBar.hide()
            }
            ViewSubtaskEvent.DeleteSubtask -> {
                nestedScrollView.show()
                progressBar.hide()
                findNavController().popBackStack()
            }
        }
    }
}
