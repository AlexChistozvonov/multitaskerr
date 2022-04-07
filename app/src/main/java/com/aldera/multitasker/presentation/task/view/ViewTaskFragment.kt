package com.aldera.multitasker.presentation.task.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.ViewTaskFragmentBinding
import com.aldera.multitasker.presentation.task.CustomRecyclerAdapterTask
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ViewTaskFragment : Fragment(R.layout.view_task_fragment) {
    private val binding by viewBinding(ViewTaskFragmentBinding::bind)
    private val args: ViewTaskFragmentArgs by navArgs()
    private val viewModel by viewModels<ViewTaskViewModel>()

    private val subTaskAdapter by lazy {
        CustomRecyclerAdapterTask {
            args.taskCreate.let { _ ->
                ViewTaskFragmentDirections.openEditTaskFragment(
                    it,
                    args.taskCreate,
                    args.category,
                )
            }.let { navDirections ->
                findNavController().navigateSafe(
                    navDirections
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
        initRecyclerview()
    }

    private fun init() = with(binding) {
        btnEditTask.onClick {
            findNavController().navigateSafe(
                ViewTaskFragmentDirections.openEditTaskFragment(
                    args.task,
                    args.taskCreate,
                    args.category
                )
            )
        }
        val id = args.task.id
        id?.let {
            viewModel.getViewTask(it)
            viewModel.getSubTask(it)
        }
        toolbar.apply {
            tvTitle.text = getString(R.string.task)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
            ibAction.setImageResource(R.drawable.ic_plus)
            ibAction.show()
            ibAction.onClick {
                findNavController().navigateSafe(
                    ViewTaskFragmentDirections.openCreateSubtaskFragment(
                        args.taskCreate
                    )
                )
            }
        }
        tvNameTask.text = args.taskCreate.title
        tvTitleTask.text = args.category.title
        llNameCategory.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(args.category.color))

        val itemCount: Int = subTaskAdapter.itemCount
        tvSubtaskNumber.text = itemCount.toString()
        if (etDescription.text.isNullOrEmpty()) {
            etDescription.hide()
            tvDescriptionTask.hide()
        } else {
            etDescription.text = args.taskCreate.description
        }
        etPeriodOfExecutionTask.text = args.taskCreate.deadline
        Glide.with(requireContext()).load(args.taskCreate.performer?.avatar).into(ivAvatarExecutor)
        tvNameExecutor.text = args.taskCreate.performer?.name
        initRgGroup()
    }

    private fun handleSuccess(state: ViewTaskViewState) = with(binding) {
        state.task?.let { task ->
            tvNameAuthor.text = task.author?.name
            Glide.with(requireContext()).load(task.author?.avatar).into(ivAvatarAuthor)
            tvCreated.text = getString(R.string.created, task.createdAt)
            if (task.updatedAt != null) {
                tvUpdated.text = getString(R.string.updated, task.updatedAt)
            } else {
                tvUpdated.hide()
            }
        }
    }

    private fun initRgGroup() = with(binding) {
        when (args.taskCreate.importance) {
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

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: ViewTaskViewState) = with(binding) {

        when (state.event) {

            is ViewTaskEvent.Error -> {
                nestedScrollView.show()
                progressBar.hide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            ViewTaskEvent.Init -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            ViewTaskEvent.Loading -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            is ViewTaskEvent.Success -> {
                handleSuccess(state)
                nestedScrollView.hide()
                progressBar.show()
            }
            is ViewTaskEvent.UpdateSubTaskTask -> {
                nestedScrollView.hide()
                progressBar.show()
                updateList(state)
            }
        }
    }

    private fun initRecyclerview() = with(binding) {
        val recyclerView: RecyclerView = recyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = subTaskAdapter
    }

    private fun updateList(state: ViewTaskViewState) {
        state.subTask?.let {
            subTaskAdapter.setData(it, args.category.color, args.category.title)
        }
    }
}
