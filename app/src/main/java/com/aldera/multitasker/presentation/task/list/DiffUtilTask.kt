package com.aldera.multitasker.presentation.task.list

import androidx.recyclerview.widget.DiffUtil
import com.aldera.multitasker.data.models.TaskResponse

class DiffUtilTask(
    private val oldList: List<TaskResponse>,
    private val newList: List<TaskResponse>
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
            oldList[oldItemPosition].performer != newList[newItemPosition].performer -> {
                false
            }
            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }
            oldList[oldItemPosition].importance != newList[newItemPosition].importance -> {
                false
            }
            else -> true
        }
    }
}
