package com.aldera.multitasker.presentation.my

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.CategoryResponse

class CustomRecyclerAdapterCategory(private val listener: (id: CategoryResponse) -> Unit) :
    RecyclerView.Adapter<CustomRecyclerAdapterCategory.MyViewHolder>() {

    private var categoryList = mutableListOf<CategoryResponse>()

    fun setCategory(listCategory: List<CategoryResponse>) {
        this.categoryList = listCategory.toMutableList()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val titleCategory: TextView = itemView.findViewById(R.id.tv_title_category)
        val numberOfProjects: TextView = itemView.findViewById(R.id.tv_number_of_projects)
        val colorCategory: ImageView = itemView.findViewById(R.id.iv_color_category)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.invoke(categoryList[absoluteAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_category, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder) {

        colorCategory.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(categoryList[position].color))
        titleCategory.text = categoryList[position].title
        numberOfProjects.text = categoryList[position].projectsCount?.let {
            itemView.resources.getQuantityString(
                R.plurals.plurals_project,
                it, categoryList[position].projectsCount
            )
        }
    }

    override fun getItemCount() = categoryList.size
}
