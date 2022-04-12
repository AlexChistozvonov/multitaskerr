package com.aldera.multitasker.presentation.appointed

import androidx.recyclerview.widget.DiffUtil
import com.aldera.multitasker.data.models.UserTaskResponse

class DiffUtilUserTask(
    private val oldList: List<UserTaskResponse>,
    private val newList: List<UserTaskResponse>
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
            oldList[oldItemPosition].performer != newList[newItemPosition].performer -> {
                false
            }
            oldList[oldItemPosition].importance != newList[newItemPosition].importance -> {
                false
            }
            else -> true
        }
    }
}
