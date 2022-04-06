package com.aldera.multitasker.presentation.my

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R
import com.aldera.multitasker.data.models.CategoryResponse

class CustomRecyclerAdapterCategory(private val listener: (id: CategoryResponse) -> Unit) :
    RecyclerView.Adapter<CustomRecyclerAdapterCategory.MyViewHolder>() {

    private var oldCategoryList = listOf<CategoryResponse>()

    fun setCategory(newCategoryList: List<CategoryResponse>) {
        val diffUtil = DiffUtilCategory(oldCategoryList, newCategoryList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldCategoryList = newCategoryList
        diffResults.dispatchUpdatesTo(this)
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
            listener.invoke(oldCategoryList[layoutPosition])
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
            ColorStateList.valueOf(Color.parseColor(oldCategoryList[position].color))
        titleCategory.text = oldCategoryList[position].title
        numberOfProjects.text = oldCategoryList[position].projectsCount?.let {
            itemView.resources.getQuantityString(
                R.plurals.plurals_project,
                it, oldCategoryList[position].projectsCount
            )
        }
    }

    override fun getItemCount() = oldCategoryList.size
}
