package com.aldera.multitasker.presentation.task.list

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.TaskResponse
import com.aldera.multitasker.data.models.imageUrl
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide

class RecyclerAdapterTask(private val listener: (id: TaskResponse) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterTask.MyViewHolder>() {

    private var oldTaskList = listOf<TaskResponse>()
    private var nameCategory: String? = null
    private var color: String? = null

    fun setData(newListTask: List<TaskResponse>, color: String?, nameCategory: String?) {
        val diffUtil = DiffUtilTask(oldTaskList, newListTask)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldTaskList = newListTask
        diffResults.dispatchUpdatesTo(this)
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
            listener.invoke(oldTaskList[layoutPosition])
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
        nameTask.text = oldTaskList[position].title
        colorCategory.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))

        when (oldTaskList[position].importance) {
            Constants.IMPORTANCE_1 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_1).into(importance)
            }
            Constants.IMPORTANCE_2 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_2).into(importance)
            }
            Constants.IMPORTANCE_3 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_3).into(importance)
            }
            Constants.IMPORTANCE_4 -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_4).into(importance)
            }
            else -> {
                Glide.with(itemView.context).load(R.drawable.ic_urgently_4).into(importance)
            }
        }
        Glide.with(itemView.context).load(oldTaskList[position].performer?.avatar?.imageUrl())
            .circleCrop().into(avatar)
        if (oldTaskList[position].performer?.name != null) {
            executor.text = oldTaskList[position].performer?.name
        } else {
            executor.text = oldTaskList[position].performer?.email
        }
    }

    override fun getItemCount() = oldTaskList.size
}
