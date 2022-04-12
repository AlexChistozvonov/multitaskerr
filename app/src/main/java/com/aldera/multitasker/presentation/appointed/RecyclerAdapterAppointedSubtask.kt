package com.aldera.multitasker.presentation.appointed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.UserTaskResponse
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide

class RecyclerAdapterAppointedSubtask(private val listener: (id: UserTaskResponse) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterAppointedSubtask.MyViewHolder>() {

    private var oldSubtaskList = listOf<UserTaskResponse>()

    fun setSubtask(newCategoryList: List<UserTaskResponse>) {
        val diffUtil = DiffUtilUserTask(oldSubtaskList, newCategoryList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldSubtaskList = newCategoryList
        diffResults.dispatchUpdatesTo(this)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val titleSubtask: TextView = itemView.findViewById(R.id.tv_name_subtask)
        val descriptionSubtask: TextView = itemView.findViewById(R.id.tv_description_subtask)
        val importance: ImageView = itemView.findViewById(R.id.iv_importance)
        val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val executor: TextView = itemView.findViewById(R.id.tv_executor)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.invoke(oldSubtaskList[layoutPosition])
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterAppointedSubtask.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_appointed_subtask, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: RecyclerAdapterAppointedSubtask.MyViewHolder,
        position: Int
    ) = with(holder) {
        titleSubtask.text = oldSubtaskList[position].title
//         скоро появится
//         descriptionSubtask.text = oldTaskList[position].description
        when (oldSubtaskList[position].importance) {
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
        Glide.with(itemView.context).load(oldSubtaskList[position].performer?.avatar).into(avatar)
        executor.text = oldSubtaskList[position].performer?.name
    }

    override fun getItemCount() = oldSubtaskList.size
}
