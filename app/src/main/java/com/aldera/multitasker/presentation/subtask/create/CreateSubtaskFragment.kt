package com.aldera.multitasker.presentation.subtask.create

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.CreateSubtaskFragmentBinding
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
class CreateSubtaskFragment : Fragment(R.layout.create_subtask_fragment) {
    private val binding by viewBinding(CreateSubtaskFragmentBinding::bind)
    private val viewModel by viewModels<CreateSubtaskViewModel>()
    private val args: CreateSubtaskFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
    }

    private fun init() = with(binding) {
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        etDescription.doOnTextChanged { text, _, _, _ -> viewModel.onDescriptionTextChanged(text.toString()) }
        etPeriodOfExecutionSubtask.doOnTextChanged { text, _, _, _ ->
            viewModel.onDeadlineTextChanged(
                text.toString()
            )
        }
        viewModel.getExecutor()
        val id = args.task?.id
        btnCreateSubtask.onClick { id?.let { viewModel.createSubtask(it) } }
        btnCancel.onClick { findNavController().popBackStack() }
        toolbar.apply {
            tvTitle.text = getString(R.string.create_subtask_)
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

    private fun chooseExecutor(data: List<String?>) = with(binding) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            data
        )

        atvExecutor.setAdapter(adapter)
        atvExecutor.setOnItemClickListener { _, _, _, _ ->
            viewModel.onEmailChange(atvExecutor.text.toString())
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: CreateSubtaskViewState) = with(binding) {
        btnCreateSubtask.isEnabled =
            !etEditName.text.isNullOrEmpty() && !etPeriodOfExecutionSubtask.text.isNullOrEmpty()
        when (state.event) {
            is CreateSubtaskEvent.Error -> {
                progressBar.hide()
                nestedScrollView.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            is CreateSubtaskEvent.GetExecutor -> {
                progressBar.hide()
                nestedScrollView.show()
                state.userList?.let { it -> chooseExecutor(it.map { it.email }) }
            }
            is CreateSubtaskEvent.GetExecutorError -> {
                progressBar.hide()
                nestedScrollView.show()
            }
            CreateSubtaskEvent.Loading -> {
                progressBar.show()
                nestedScrollView.hide()
            }
            CreateSubtaskEvent.Success -> {
                progressBar.hide()
                nestedScrollView.show()
                findNavController().popBackStack()
            }
            is CreateSubtaskEvent.DeadlineChanged -> {
                progressBar.hide()
                nestedScrollView.show()
                initImportance()
            }
            else -> {
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
        val diff = selectedDate.dayOfYear - current.dayOfYear
        when {
            diff < 0 || diff in 0..1 -> rbUrgently4.isChecked = true
            diff in 1..2 ->
                rbUrgently2.isChecked = true
            else -> rbUrgently1.isChecked = true
        }
    }
}
