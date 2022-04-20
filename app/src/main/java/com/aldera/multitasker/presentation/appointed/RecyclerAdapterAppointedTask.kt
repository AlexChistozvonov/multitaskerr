package com.aldera.multitasker.presentation.appointed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.UserTaskResponse
import com.aldera.multitasker.data.models.imageUrl
import com.aldera.multitasker.ui.extension.show
import com.aldera.multitasker.ui.util.Constants
import com.bumptech.glide.Glide

class RecyclerAdapterAppointedTask(private val listener: (id: UserTaskResponse) -> Unit) :
    RecyclerView.Adapter<RecyclerAdapterAppointedTask.MyViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private var oldTaskList = listOf<UserTaskResponse>()
    private var userSubtask = listOf<UserTaskResponse>()
    private var amountSubtask: String? = null
    private var itemCount: Int? = null

    private val listAdapterSubtask by lazy {
        RecyclerAdapterAppointedSubtask {}
    }

    fun setTask(
        newCategoryList: List<UserTaskResponse>,
        amountSubtask: String?,
        itemCount: Int,
        userSubtask: List<UserTaskResponse>?
    ) {
        val diffUtil = DiffUtilUserTask(oldTaskList, newCategoryList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldTaskList = newCategoryList
        diffResults.dispatchUpdatesTo(this)
        this.amountSubtask = amountSubtask.toString()
        this.itemCount = itemCount
        if (userSubtask != null) {
            this.userSubtask = userSubtask
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val titleTask: TextView = itemView.findViewById(R.id.tv_name_task)
        val descriptionTask: TextView = itemView.findViewById(R.id.tv_description_task)
        val recycler: RecyclerView = itemView.findViewById(R.id.recyclerViewSubtask)
        val importance: ImageView = itemView.findViewById(R.id.iv_importance)
        val avatar: ImageView = itemView.findViewById(R.id.iv_avatar)
        val executor: TextView = itemView.findViewById(R.id.tv_executor)
        val numberSubtask: TextView = itemView.findViewById(R.id.tv_number_subtask)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.invoke(oldTaskList[layoutPosition])
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterAppointedTask.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_appointed_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.titleTask.text = oldTaskList[position].title
//         скоро появится
//         descriptionTask.text = oldTaskList[position].description
        when (oldTaskList[position].importance) {
            Constants.IMPORTANCE_1 -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_urgently_1)
                    .into(holder.importance)
            }
            Constants.IMPORTANCE_2 -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_urgently_2)
                    .into(holder.importance)
            }
            Constants.IMPORTANCE_3 -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_urgently_3)
                    .into(holder.importance)
            }
            Constants.IMPORTANCE_4 -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_urgently_4)
                    .into(holder.importance)
            }
            else -> {
                Glide.with(holder.itemView.context).load(R.drawable.ic_urgently_4)
                    .into(holder.importance)
            }
        }
        Glide.with(holder.itemView.context)
            .load(oldTaskList[position].performer?.avatar?.imageUrl()).circleCrop()
            .into(holder.avatar)
        holder.executor.text = oldTaskList[position].performer?.name
        itemCount?.let {
            if (it > 0) {
                holder.numberSubtask.show()
                holder.numberSubtask.text = this@RecyclerAdapterAppointedTask.amountSubtask
                val childLayoutManager =
                    LinearLayoutManager(holder.recycler.context, RecyclerView.HORIZONTAL, false)
                childLayoutManager.initialPrefetchItemCount = Constants.IMPORTANCE_3
                holder.recycler.show()
                holder.recycler.apply {
                    layoutManager = childLayoutManager
                    adapter = listAdapterSubtask
                    setRecycledViewPool(viewPool)
                    listAdapterSubtask.setSubtask(userSubtask)
                }
            }
        }
    }

    override fun getItemCount() = oldTaskList.size
}
