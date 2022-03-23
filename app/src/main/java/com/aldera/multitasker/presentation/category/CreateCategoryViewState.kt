package com.aldera.multitasker.presentation.category

import com.aldera.multitasker.R

sealed class CreateCategoryEvent {
    object Success : CreateCategoryEvent()
    object Loading : CreateCategoryEvent()
    object Init : CreateCategoryEvent()
    data class Error(val error: Exception?) : CreateCategoryEvent()
    data class TitleChanged(val text: String) : CreateCategoryEvent()
    data class ColorChanged(val colorItem: ColorItem) : CreateCategoryEvent()
}

data class CreateCategoryViewState(
    val error: Exception? = null,
    val titleText: String = "",
    val colorId: Int = R.color.category_yellow,
    val colorText: String = "#FFC700",
    val loading: Boolean = false,
    val loader: Boolean = false,
    val event: CreateCategoryEvent = CreateCategoryEvent.Init
) {
    fun applyEvent(event: CreateCategoryEvent) = when (event) {
        is CreateCategoryEvent.ColorChanged -> copy(
            colorId = event.colorItem.background,
            colorText = event.colorItem.color,
            event = event
        )
        is CreateCategoryEvent.Error -> copy(error = event.error, event = event)
        CreateCategoryEvent.Init -> copy(event = event)
        CreateCategoryEvent.Loading -> copy(loader = true, event = event)
        CreateCategoryEvent.Success -> copy(loader = true, event = event)
        is CreateCategoryEvent.TitleChanged -> copy(titleText = event.text, event = event)
    }
}
