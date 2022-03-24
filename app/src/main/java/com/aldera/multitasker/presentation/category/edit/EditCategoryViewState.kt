package com.aldera.multitasker.presentation.category.edit

import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.CategoryResponse
import com.aldera.multitasker.presentation.category.ColorItem

sealed class EditCategoryEvent {
    object Success : EditCategoryEvent()
    object Loading : EditCategoryEvent()
    object Init : EditCategoryEvent()
    data class Error(val error: Exception?) : EditCategoryEvent()
    data class TitleChanged(val text: String) : EditCategoryEvent()
    data class ColorChanged(val colorItem: ColorItem) : EditCategoryEvent()
}

data class EditCategoryViewState(
    val category: CategoryResponse? = null,
    val error: Exception? = null,
    val titleText: String = "",
    val colorId: Int = R.color.category_yellow,
    val colorText: String = "",
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: EditCategoryEvent = EditCategoryEvent.Init
) {
    fun applyEvent(event: EditCategoryEvent) = when (event) {
        is EditCategoryEvent.ColorChanged -> copy(
            colorId = event.colorItem.background,
            colorText = event.colorItem.color,
            event = event
        )
        is EditCategoryEvent.Error -> copy(error = event.error, event = event)
        EditCategoryEvent.Init -> copy(event = event)
        EditCategoryEvent.Loading -> copy(loader = true, event = event)
        EditCategoryEvent.Success -> copy(loader = true, event = event)
        is EditCategoryEvent.TitleChanged -> copy(titleText = event.text, event = event)
    }
}
