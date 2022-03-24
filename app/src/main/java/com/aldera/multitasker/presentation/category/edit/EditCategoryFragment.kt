package com.aldera.multitasker.presentation.category.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.EditCategoryFragmentBinding
import com.aldera.multitasker.presentation.category.ColorItem
import com.aldera.multitasker.presentation.category.CustomRecyclerAdapterColorCategory
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditCategoryFragment : Fragment(R.layout.edit_category_fragment) {
    private val binding by viewBinding(EditCategoryFragmentBinding::bind)
    private val viewModel by viewModels<EditCategoryViewModel>()
    private val args: EditCategoryFragmentArgs by navArgs()
    private var colorList = ArrayList<ColorItem>()
    lateinit var adapter: CustomRecyclerAdapterColorCategory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        val id = args.category.id
        val title = args.category.title
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        btnSave.onClick { id?.let { viewModel.editCategory(it) } }
        toolbar.apply {
            tvTitle.text = getString(R.string._edit)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
        etEditName.setText(title)
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: EditCategoryViewState) = with(binding) {
        fun showHide() {
            progressBar.hide()
            btnSave.show()
            etEditName.show()
            btnDelete.show()
            rvColorCategory.show()
            tilName.show()
        }
        btnSave.isEnabled = !etEditName.text.isNullOrEmpty()
        when (state.event) {
            is EditCategoryEvent.ColorChanged -> {
                addGridItem(state.colorText)
                showHide()
            }
            is EditCategoryEvent.Error -> {
                showHide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            EditCategoryEvent.Init -> {
                showHide()
            }
            EditCategoryEvent.Loading -> {
                progressBar.show()
                btnSave.hide()
                etEditName.hide()
                btnDelete.hide()
                rvColorCategory.hide()
                tilName.hide()
            }
            EditCategoryEvent.Success -> {
                findNavController().navigateSafe(EditCategoryFragmentDirections.openMyFragment())
                showHide()
            }
            is EditCategoryEvent.TitleChanged -> showHide()
        }
    }

    private fun initRecyclerview() = with(binding) {
        adapter = CustomRecyclerAdapterColorCategory {
            viewModel.onSelectedColorChanged(it)
        }
        val recyclerView: RecyclerView = rvColorCategory
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, EditCategoryFragment.SPAN_COUNT)
        recyclerView.adapter = adapter
        addGridItem(args.category.color)
        colorList.addAll(Constants.gridList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addGridItem(selectedColor: String?) {
        adapter.setColorItemList(colorItemList = colorList, selectedColor = selectedColor)
        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val SPAN_COUNT = 5
    }
}
