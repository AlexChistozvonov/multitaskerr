package com.aldera.multitasker.presentation.subtask.edit

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
import com.aldera.multitasker.databinding.EditSubtaskFragmentBinding
import com.aldera.multitasker.presentation.DatePickerDialogFragment
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.ConstantDateDialogFragment
import com.aldera.multitasker.ui.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class EditSubtaskFragment : Fragment(R.layout.edit_subtask_fragment) {
    private val binding by viewBinding(EditSubtaskFragmentBinding::bind)
    private val viewModel by viewModels<EditSubtaskViewModel>()
    private val args: EditSubtaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() = with(binding) {
        val id = args.task?.id
        btnDelete.onClick { id?.let { viewModel.deleteTask(it) } }
        btnSave.onClick { id?.let { viewModel.editSubtask(it) } }
        etEditName.setText(args.createSubtask.title)
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        etDescription.setText(args.createSubtask.description)
        etDescription.doOnTextChanged { text, _, _, _ -> viewModel.onDescriptionTextChanged(text.toString()) }
        etPeriodOfExecutionSubtask.setText(args.createSubtask.deadline)
        etPeriodOfExecutionSubtask.doOnTextChanged { text, _, _, _ ->
            viewModel.onDeadlineTextChanged(
                text.toString()
            )
        }
        toolbar.apply {
            tvTitle.text = getString(R.string._edit)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
        etPeriodOfExecutionSubtask.onClick {
            val datePickerFragment = DatePickerDialogFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                ConstantDateDialogFragment.REQUEST_KEY,
                viewLifecycleOwner
            ) { resultKey, bundle ->
                if (resultKey == ConstantDateDialogFragment.REQUEST_KEY) {
                    val date = bundle.getString(ConstantDateDialogFragment.SELECTED_DATE)
                    etPeriodOfExecutionSubtask.setText(date)
                }
            }
            datePickerFragment.show(
                supportFragmentManager,
                ConstantDateDialogFragment.DATE_PICKER_FRAGMENT
            )
        }
        initRgGroup()
    }

    private fun initRgGroup() = with(binding) {
        rgGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_urgently4 -> {
                    rbUrgently4.isChecked = true
                    viewModel.onImportanceChanged(Constants.IMPORTANCE_4)
                }
                R.id.rb_urgently3 -> {
                    rbUrgently3.isChecked = true
                    viewModel.onImportanceChanged(Constants.IMPORTANCE_3)
                }
                R.id.rb_urgently2 -> {
                    rbUrgently2.isChecked = true
                    viewModel.onImportanceChanged(Constants.IMPORTANCE_2)
                }
                R.id.rb_urgently1 -> {
                    rbUrgently1.isChecked = true
                    viewModel.onImportanceChanged(Constants.IMPORTANCE_1)
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: EditSubtaskViewState) = with(binding) {
        btnSave.isEnabled =
            !etEditName.text.isNullOrEmpty() && !etPeriodOfExecutionSubtask.text.isNullOrEmpty()
        when (state.event) {
            is EditSubtaskEvent.DeadlineChanged -> {
                progressBar.hide()
                nestedScrollView.show()
                initImportance()
            }
            is EditSubtaskEvent.DescriptionChanged -> {
                progressBar.hide()
                nestedScrollView.show()
            }
            is EditSubtaskEvent.Error -> {
                progressBar.hide()
                nestedScrollView.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            is EditSubtaskEvent.Importance -> {
                progressBar.hide()
                nestedScrollView.show()
            }
            EditSubtaskEvent.Init -> {
                progressBar.hide()
                nestedScrollView.show()
            }
            EditSubtaskEvent.Loading -> {
                progressBar.show()
                nestedScrollView.hide()
            }
            EditSubtaskEvent.Success -> {
                progressBar.hide()
                nestedScrollView.show()
                findNavController().popBackStack()
            }
            is EditSubtaskEvent.TitleChanged -> {
                progressBar.hide()
                nestedScrollView.show()
            }
        }
    }

    private fun initImportance() = with(binding) {
        val current = LocalDate.now()
        val selected = etPeriodOfExecutionSubtask.text.toString()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        val selectedDate = LocalDate.from(formatter.parse(selected))
        when (selectedDate.dayOfYear - current.dayOfYear) {
            in 0..1 -> rbUrgently4.isChecked = true
            in 1..2 ->
                rbUrgently2.isChecked = true
            else -> rbUrgently1.isChecked = true
        }
    }
}
