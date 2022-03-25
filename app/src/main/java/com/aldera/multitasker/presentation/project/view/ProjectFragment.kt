package com.aldera.multitasker.presentation.project.view

import android.annotation.SuppressLint
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
import com.aldera.multitasker.databinding.ProjectFragmentBinding
import com.aldera.multitasker.presentation.project.CustomRecyclerAdapterProject
import com.aldera.multitasker.presentation.project.ProjectEvent
import com.aldera.multitasker.presentation.project.ProjectViewState
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProjectFragment : Fragment(R.layout.project_fragment) {
    private val binding by viewBinding(ProjectFragmentBinding::bind)
    private val viewModel by viewModels<ProjectViewModel>()
    private val projectAdapter by lazy {
        CustomRecyclerAdapterProject {
            findNavController().navigateSafe(
                ProjectFragmentDirections.openEditProjectFragment(
                    it,
                    args.category
                )
            )
        }
    }
    private val args: ProjectFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
        initRecyclerview()
    }

    private fun init() = with(binding) {
        val id = args.category.id
        val title = args.category.title
        llCreate.onClick {
            findNavController().navigateSafe(
                ProjectFragmentDirections.openCreateProjectFragment(args.category)
            )
        }
        id?.let { viewModel.getProject(it) }
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
                    ProjectFragmentDirections.openEditCategory(
                        args.category
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: ProjectViewState) = with(binding) {
        when (state.event) {
            is ProjectEvent.Error -> {
                progressBar.hide()
                recyclerview.show()
                llCreate.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            ProjectEvent.Loading -> {
                progressBar.show()
                recyclerview.hide()
                llCreate.show()
            }
            is ProjectEvent.Success -> {
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
        recyclerView.adapter = projectAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(state: ProjectViewState) {
        state.project?.let {
            projectAdapter.setProject(it, args.category.color, args.category.title)
            projectAdapter.notifyDataSetChanged()
        }
    }
}
