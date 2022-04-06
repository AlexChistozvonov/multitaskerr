package com.aldera.multitasker.presentation.project

import androidx.recyclerview.widget.DiffUtil
import com.aldera.multitasker.data.models.ProjectResponse

class DiffUtilProject(
    private val oldList: List<ProjectResponse>,
    private val newList: List<ProjectResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }
            else -> true
        }
    }
}
