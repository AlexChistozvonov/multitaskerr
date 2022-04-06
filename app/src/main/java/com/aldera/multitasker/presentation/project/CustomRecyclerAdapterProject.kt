package com.aldera.multitasker.presentation.project

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.ProjectResponse

class CustomRecyclerAdapterProject(private val onClick: (ProjectResponse) -> Unit) :
    RecyclerView.Adapter<CustomRecyclerAdapterProject.MyViewHolder>() {

    private var oldProjectList = listOf<ProjectResponse>()
    private var color: String? = null
    private var title: String? = null

    fun setProject(newProjectList: List<ProjectResponse>, color: String?, title: String?) {
        val diffUtil = DiffUtilProject(oldProjectList, newProjectList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldProjectList = newProjectList
        diffResults.dispatchUpdatesTo(this)
        this.color = color.toString()
        this.title = title.toString()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleProject: TextView = itemView.findViewById(R.id.tv_title_project)
        val nameCategory: TextView = itemView.findViewById(R.id.tv_name_category)
        val colorCategory: LinearLayout = itemView.findViewById(R.id.ll_name_category)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onClick.invoke(oldProjectList[layoutPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_project, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder) {
        nameCategory.text = oldProjectList[position].title
        colorCategory.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
        titleProject.text = title
    }

    override fun getItemCount() = oldProjectList.size
}
