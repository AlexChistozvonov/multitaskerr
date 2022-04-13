package com.aldera.multitasker.presentation.category.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.CreateCategoryFragmentBinding
import com.aldera.multitasker.presentation.category.ColorItem
import com.aldera.multitasker.presentation.category.CreateCategoryEvent
import com.aldera.multitasker.presentation.category.CreateCategoryViewState
import com.aldera.multitasker.presentation.category.RecyclerAdapterColorCategory
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import com.aldera.multitasker.ui.util.ConstantGridList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CreateCategoryFragment : Fragment(R.layout.create_category_fragment) {
    private val binding by viewBinding(CreateCategoryFragmentBinding::bind)
    var colorList = ArrayList<ColorItem>()
    lateinit var adapter: RecyclerAdapterColorCategory
    private val viewModel by viewModels<CreateCategoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerview()
        initView()
        initObservers()
    }

    private fun initView() = with(binding) {
        etEditName.doOnTextChanged { text, _, _, _ -> viewModel.onTitleTextChanged(text.toString()) }
        btnCreateCategory.onClick {
            viewModel.createCategory()
        }
        btnCancel.onClick { findNavController().popBackStack() }
        toolbar.apply {
            tvTitle.text = getString(R.string.create_category)
            ibNavigationIcon.setImageResource(R.drawable.ic_chevron_left)
            ibNavigationIcon.onClick {
                findNavController().popBackStack()
            }
        }
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: CreateCategoryViewState) = with(binding) {
        fun showHide() {
            progressBar.hide()
            btnCreateCategory.show()
            etEditName.show()
            btnCancel.show()
            rvColorCategory.show()
            tilName.show()
        }
        btnCreateCategory.isEnabled = !etEditName.text.isNullOrEmpty()
        when (state.event) {
            is CreateCategoryEvent.ColorChanged -> {
                addGridItem(state.colorId)
                showHide()
            }
            is CreateCategoryEvent.Error -> {
                showHide()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            CreateCategoryEvent.Init -> showHide()
            CreateCategoryEvent.Loading -> {
                progressBar.show()
                btnCreateCategory.hide()
                etEditName.hide()
                btnCancel.hide()
                rvColorCategory.hide()
                tilName.hide()
            }
            CreateCategoryEvent.Success -> {
                findNavController().popBackStack()
                showHide()
            }
            is CreateCategoryEvent.TitleChanged -> showHide()
        }
    }

    private fun initRecyclerview() = with(binding) {
        adapter = RecyclerAdapterColorCategory {
            viewModel.onSelectedColorChanged(it)
        }
        val recyclerView: RecyclerView = rvColorCategory
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        recyclerView.adapter = adapter
        addGridItem(viewModel.uiState.value.colorId)
        colorList.addAll(ConstantGridList.gridList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addGridItem(selectedColorId: Int) {
        adapter.setColorItemList(colorItemList = colorList, selectedColorId = selectedColorId)
        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val SPAN_COUNT = 5
    }
}
