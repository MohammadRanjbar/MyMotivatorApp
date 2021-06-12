package com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymotivator.R
import com.example.mymotivator.databinding.RecyclerCustomItemsBinding

class SettingRecyclerAdapter(private var listener:SettingsClickedListener):RecyclerView.Adapter<SettingRecyclerAdapter.ViewHolder>() {
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

    inner class  ViewHolder(private var binding: RecyclerCustomItemsBinding):RecyclerView.ViewHolder(binding.root){


        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val item = items[position]
                    listener.onSettingsItemClicked(item.first)
                }
            }
        }
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

    interface SettingsClickedListener{
        fun onSettingsItemClicked(item:String)
    }
}