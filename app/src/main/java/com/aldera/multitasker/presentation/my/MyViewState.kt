package com.aldera.multitasker.presentation.my

import com.aldera.multitasker.data.models.CategoryResponse

sealed class MyEvent {
    object Loading : MyEvent()
    data class Success(val category: List<CategoryResponse>?) : MyEvent()
    data class Error(val error: Exception?) : MyEvent()
    data class Id(val text: String) : MyEvent()
}

data class MyViewState(
    val id: String? = null,
    val error: Exception? = null,
    val loading: Boolean = false,
    val category: List<CategoryResponse>? = null,
    val event: MyEvent = MyEvent.Loading
) {
    fun applyEvent(event: MyEvent) = when (event) {
        is MyEvent.Error -> copy(error = event.error, loading = false, event = event)
        MyEvent.Loading -> copy(loading = true, event = event)
        is MyEvent.Success -> copy(category = event.category, loading = false, event = event)
        is MyEvent.Id -> copy(id = event.text, event = event)
    }
}
