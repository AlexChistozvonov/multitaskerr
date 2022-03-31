package com.aldera.multitasker.presentation.task.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.TaskFragmentBinding
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.task_fragment) {
    private val binding by viewBinding(TaskFragmentBinding::bind)
    private val args: TaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() = with(binding) {
        Timber.e("project ${args.project}")
        llCreate.onClick {
            findNavController().navigateSafe(
                TaskFragmentDirections.openCreateTaskFragment(args.project)
            )
        }
        val title = args.project.title
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
}
