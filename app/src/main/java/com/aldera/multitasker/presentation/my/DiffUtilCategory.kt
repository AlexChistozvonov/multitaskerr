package com.aldera.multitasker.presentation.my

import androidx.recyclerview.widget.DiffUtil
import com.aldera.multitasker.data.models.CategoryResponse

class DiffUtilCategory(
    private val oldList: List<CategoryResponse>,
    private val newList: List<CategoryResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id != newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }
            oldList[oldItemPosition].projectsCount != newList[newItemPosition].projectsCount -> {
                false
            }
            oldList[oldItemPosition].color != newList[newItemPosition].color -> {
                false
            }
            else -> true
        }
    }
}
