package com.aldera.multitasker.presentation.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aldera.multitasker.R
import com.aldera.multitasker.databinding.MyFragmentBinding
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.navigateSafe
import com.aldera.multitasker.ui.extension.onClick
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.extension.showGeneralErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MyFragment : Fragment(R.layout.my_fragment) {
    private val binding by viewBinding(MyFragmentBinding::bind)
    private val viewModel by viewModels<MyViewModel>()
    private val listAdapter by lazy {
        CustomRecyclerAdapterCategory {
            findNavController().navigateSafe(MyFragmentDirections.openProjectFragment(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initObservers()
        initRecyclerview()
    }

    private fun init() = with(binding) {
        ivPlus.onClick {
            findNavController().navigateSafe(MyFragmentDirections.openCreateCategory())
        }
        viewModel.getCategory()
    }

    private fun initObservers() {
        viewModel.uiState.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: MyViewState) = with(binding) {
        when (state.event) {
            is MyEvent.Error -> {
                progressBar.hide()
                recyclerView.show()
                showGeneralErrorDialog(
                    context = requireContext(),
                    exception = state.error
                )
            }
            MyEvent.Loading -> {
                progressBar.show()
                recyclerView.hide()
            }
            is MyEvent.Success -> {
                progressBar.hide()
                recyclerView.show()
                updateList(state)
            }
            is MyEvent.Id -> {}
        }
    }

    private fun initRecyclerview() = with(binding) {
        val recyclerView: RecyclerView = recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter
    }

    private fun updateList(state: MyViewState) {
        state.category?.let {
            listAdapter.setCategory(it)
        }
    }
}
