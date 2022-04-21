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
import com.aldera.multitasker.data.models.imageUrl
import com.aldera.multitasker.databinding.ViewTaskFragmentBinding
import com.aldera.multitasker.presentation.task.list.RecyclerAdapterTask
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
class ViewTaskFragment : Fragment(R.layout.view_task_fragment) {
    private val binding by viewBinding(ViewTaskFragmentBinding::bind)
    private val args: ViewTaskFragmentArgs by navArgs()
    private val viewModel by viewModels<ViewTaskViewModel>()

    private val subTaskAdapter by lazy {
        RecyclerAdapterTask {
            findNavController().navigateSafe(
                ViewTaskFragmentDirections.openViewSubtaskFragment(args.task, args.category, it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
        initRecyclerview()
    }

    private fun init() = with(binding) {
        val id = args.task?.id
        id?.let {
            viewModel.getViewTask(it)
            viewModel.getSubTask(it)
        }
        btnDelete.onClick {
            id?.let {
                viewModel.deleteTask(it)
            }
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
                    ViewTaskFragmentDirections.openCreateSubtaskFragment(args.task)
                )
            }
        }
        tvTitleTask.text = args.category.title
        llNameCategory.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(args.category.color))
    }

    private fun handleSuccess(state: ViewTaskViewState) = with(binding) {
        btnEditTask.onClick {
            findNavController().navigateSafe(
                ViewTaskFragmentDirections.openEditTaskFragment(
                    state.task,
                    args.category
                )
            )
        }

        tvNameTask.text = state.task?.title

        if (state.task?.description.isNullOrEmpty()) {
            etDescription.hide()
            tvDescriptionTask.hide()
        } else {
            etDescription.text = args.task?.description
        }

        val parser = SimpleDateFormat(Constants.DATE_FORMAT_PARSER, Locale(Constants.DATE_LOCALE))
        val formatter = SimpleDateFormat(Constants.DATE_FORMAT_MMMM, Locale(Constants.DATE_LOCALE))
        val formatterTime =
            SimpleDateFormat(Constants.DATE_FORMAT_HH, Locale(Constants.DATE_LOCALE))
        val outputDeadline = formatter.format(parser.parse(state.task?.deadline))
        val outputCreated = formatterTime.format(parser.parse(state.task?.createdAt))
        etPeriodOfExecutionTask.text = outputDeadline.toString()

        state.task?.let { task ->
            tvNameAuthor.text = task.author?.name
            Glide.with(requireContext()).load(task.author?.avatar?.imageUrl()).circleCrop()
                .into(ivAvatarAuthor)
            tvCreated.text = getString(R.string.created, outputCreated)
            if (task.updatedAt == null) {
                tvUpdated.hide()
            } else {
                val outputUpdated = formatterTime.format(parser.parse(state.task.updatedAt))
                tvUpdated.text = getString(R.string.updated, outputUpdated)
            }
        }
        Glide.with(requireContext()).load(state.task?.performer?.avatar?.imageUrl()).circleCrop()
            .into(ivAvatarExecutor)
        if (state.task?.performer?.name == null) {
            tvNameExecutor.text = state.task?.performer?.email
        } else {
            tvNameExecutor.text = state.task.performer.name
        }
        initImportance(state)
    }

    private fun initImportance(state: ViewTaskViewState) = with(binding) {
        when (state.task?.importance) {
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
                nestedScrollView.show()
                progressBar.hide()
            }
            ViewTaskEvent.Loading -> {
                nestedScrollView.hide()
                progressBar.show()
            }
            is ViewTaskEvent.Success -> {
                handleSuccess(state)
                nestedScrollView.show()
                progressBar.hide()
            }
            is ViewTaskEvent.UpdateSubTask -> {
                nestedScrollView.show()
                progressBar.hide()
                updateList(state)
            }
            ViewTaskEvent.DeleteTask -> {
                nestedScrollView.show()
                progressBar.hide()
                findNavController().popBackStack()
            }
        }
    }

    private fun initRecyclerview() = with(binding) {
        val recyclerView: RecyclerView = recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = subTaskAdapter
    }

    private fun updateList(state: ViewTaskViewState) {
        state.subTask?.let {
            subTaskAdapter.setData(it, args.category.color, args.category.title)
        }
        val itemCount: Int = subTaskAdapter.itemCount
        binding.tvSubtaskNumber.text = itemCount.toString()
    }
}
