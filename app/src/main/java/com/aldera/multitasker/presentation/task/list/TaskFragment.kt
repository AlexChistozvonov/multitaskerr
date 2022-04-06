package com.aldera.multitasker.presentation.task.list

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
import com.aldera.multitasker.databinding.TaskFragmentBinding
import com.aldera.multitasker.presentation.task.CustomRecyclerAdapterTask
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.task_fragment) {
    private val binding by viewBinding(TaskFragmentBinding::bind)
    private val args: TaskFragmentArgs by navArgs()
    private val viewModel by viewModels<TaskViewModel>()
    private val taskAdapter by lazy {
        CustomRecyclerAdapterTask {
            args.taskCreate?.let { CreateTaskResponse ->
                TaskFragmentDirections.openViewTaskFragment(
                    it,
                    CreateTaskResponse, args.category,
                )
            }?.let { NavDirections ->
                findNavController().navigateSafe(
                    NavDirections
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
        llCreate.onClick {
            findNavController().navigateSafe(
                TaskFragmentDirections.openCreateTaskFragment(args.project)
            )
        }
        val id = args.project.id
        val title = args.project.title
        id?.let { viewModel.getTask(it) }
        toolbar.apply {
            tvTitle.text = title
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
            ibAction.setImageResource(R.drawable.ic_edit)
            ibAction.show()
            ibAction.onClick {
                findNavController().navigateSafe(
                    TaskFragmentDirections.openEditProjectFragment(args.project, args.category)
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: TaskViewState) = with(binding) {
        when (state.event) {
            is TaskEvent.Error -> {
                progressBar.hide()
                recyclerview.show()
                llCreate.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            TaskEvent.Init -> {
                progressBar.hide()
                recyclerview.show()
                llCreate.show()
            }
            TaskEvent.Loading -> {
                progressBar.show()
                recyclerview.hide()
                llCreate.show()
            }
            is TaskEvent.Success -> {
                progressBar.hide()
                recyclerview.show()
                llCreate.show()
                updateList(state)
            }
        }
    }

    private fun initRecyclerview() = with(binding) {
        val recyclerView: RecyclerView = recyclerview
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = taskAdapter
    }

    private fun updateList(state: TaskViewState) {
        state.task?.let {
            taskAdapter.setData(it, args.category.color, args.category.title)
        }
    }
}
