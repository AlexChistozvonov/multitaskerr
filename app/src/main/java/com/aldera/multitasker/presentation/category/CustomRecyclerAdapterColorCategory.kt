package com.aldera.multitasker.presentation.category

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aldera.multitasker.R

class CustomRecyclerAdapterColorCategory(private val onClick: (ColorItem) -> Unit) :
    RecyclerView.Adapter<CustomRecyclerAdapterColorCategory.MyViewHolder>() {

    private var colorItemList = mutableListOf<ColorItem>()
    private var selectedColorId: Int? = null

    internal fun setColorItemList(colorItemList: MutableList<ColorItem>, selectedColorId: Int) {
        this.colorItemList = colorItemList
        this.selectedColorId = selectedColorId
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var color: ImageView = itemView.findViewById(R.id.iv_item_color)
        val check: ImageView = itemView.findViewById(R.id.iv_item_check)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onClick(colorItemList[layoutPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_item_color, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder) {
        color.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, colorItemList[position].background)
            )
        color.isSelected = selectedColorId == colorItemList[position].background
        check.isSelected = selectedColorId == colorItemList[position].background
    }

    override fun getItemCount() = colorItemList.size
}
