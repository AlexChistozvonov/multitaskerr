package com.aldera.multitasker.presentation.appointed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.AppointedFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AppointedFragment : Fragment(R.layout.appointed_fragment) {
    private val binding by viewBinding(AppointedFragmentBinding::bind)
    private val viewModel by viewModels<AppointedViewModel>()
    private val listAdapterTask by lazy {
        RecyclerAdapterAppointedTask { TODO() }
    }
    private val listAdapterSubtask by lazy {
        RecyclerAdapterAppointedSubtask { TODO() }
    }
    private val itemSubtaskCount = listAdapterSubtask.itemCount

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
        initRecyclerviewTask()
    }

    private fun init() {
        viewModel.getUserTask()
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: AppointedViewState) = with(binding) {
        when (state.event) {
            is AppointedEvent.Error -> {
                tvTaskCount.show()
                progressBar.hide()
                recyclerViewTask.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            AppointedEvent.Loading -> {
                progressBar.show()
                tvTaskCount.hide()
                recyclerViewTask.hide()
            }
            is AppointedEvent.Success -> {
                progressBar.hide()
                tvTaskCount.show()
                recyclerViewTask.show()
                updateListTask(state)
            }
            is AppointedEvent.GetTaskCount -> {
                progressBar.hide()
                tvTaskCount.show()
                recyclerViewTask.show()
                stateCount()
            }
            else -> {
                progressBar.hide()
                tvTaskCount.show()
                recyclerViewTask.show()
            }
        }
    }

    private fun stateCount() = with(binding) {
        val completedCount = viewModel.uiState.value.taskCount?.completedCount
        val totalCount = viewModel.uiState.value.taskCount?.totalCount
        tvTaskCount.text = getString(
            R.string.appointment_completed,
            totalCount.toString(),
            completedCount.toString()
        )
    }

    private fun initRecyclerviewTask() = with(binding) {
        val recyclerView: RecyclerView = recyclerViewTask
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapterTask
        }
    }

    private fun updateListTask(state: AppointedViewState) {
        state.userTask?.let {
            val amountSubtask = getString(
                R.string.subtaskCount,
                itemSubtaskCount.toString()
            )
            listAdapterTask.setTask(it, amountSubtask, itemSubtaskCount, state.userSubtask)
        }
    }
}
