package com.example.mymotivator.ui.mainFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymotivator.R
import com.example.mymotivator.databinding.RecyclerCustomItemsBinding

class SettingRecyclerAdapter:RecyclerView.Adapter<SettingRecyclerAdapter.ViewHolder>() {
    private val items = listOf("font" to R.drawable.font_icon,
    "color" to R.drawable.color_icon,"size" to R.drawable.resize_icon)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerCustomItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class  ViewHolder(private var binding: RecyclerCustomItemsBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item: Pair<String, Int>){
            binding.apply {
                itemTxt.text = item.first
                Glide.with(itemView)
                    .load(item.second)
                    .into(itemImg)
            }
        }

    }
    override fun getItemCount()= items.size
}