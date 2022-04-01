package com.aldera.multitasker.presentation.task

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.TaskResponse
import com.bumptech.glide.Glide

class CustomRecyclerAdapterTask(private val onClick: (TaskResponse) -> Unit) :
    RecyclerView.Adapter<CustomRecyclerAdapterTask.MyViewHolder>() {

    private var taskList = mutableListOf<TaskResponse>()
    private var nameCategory: String? = null
    private var color: String? = null

    fun setTask(listTask: List<TaskResponse>, color: String?, nameCategory: String?) {
        this.taskList = listTask.toMutableList()
        this.nameCategory = nameCategory.toString()
        this.color = color.toString()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleTask: TextView = itemView.findViewById(R.id.tv_title_task)
        val nameTask: TextView = itemView.findViewById(R.id.tv_name_task)
        val importance: ImageView = itemView.findViewById(R.id.iv_importance)
        val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val executor: TextView = itemView.findViewById(R.id.tv_executor)
        val colorCategory: LinearLayout = itemView.findViewById(R.id.ll_name_category)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onClick(taskList[layoutPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder) {
        titleTask.text = nameCategory
        nameTask.text = taskList[position].title
        colorCategory.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))

        when (taskList[position].importance) {
            IMPORTANCE_1 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_1).into(importance)
            }
            IMPORTANCE_2 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_2).into(importance)
            }
            IMPORTANCE_3 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_3).into(importance)
            }
            IMPORTANCE_4 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_4).into(importance)
            }
            else -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_4).into(importance)
            }
        }
        Glide.with(itemView.context).load(taskList[position].performer?.avatar).into(avatar)
        executor.text = taskList[position].performer?.name
    }

    override fun getItemCount() = taskList.size

    companion object {
        const val IMPORTANCE_4 = 4
        const val IMPORTANCE_3 = 3
        const val IMPORTANCE_2 = 2
        const val IMPORTANCE_1 = 1
    }
}
