package com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymotivator.R
import com.example.mymotivator.databinding.ColorRecyclerCustomItemBinding

class ColorRecyclerAdapter(private val context: Context, private val listener:ColorRecyclerItemClickListener):RecyclerView.Adapter<ColorRecyclerAdapter.ViewHolder>() {

    private val colors = listOf(
        R.color.black,
        R.color.white,
        R.color.color_one,
        R.color.color_two,
        R.color.color_three,
        R.color.color_fore,
        R.color.color_five,
        R.color.color_six,
        R.color.color_seven,
        R.color.color_eight
    )
 private var currentSelectedColor=0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorRecyclerAdapter.ViewHolder {
        val binding = ColorRecyclerCustomItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorRecyclerAdapter.ViewHolder, position: Int) {
        val item = colors[position]
        holder.bind(item)

    }

    inner class  ViewHolder(private var binding: ColorRecyclerCustomItemBinding):RecyclerView.ViewHolder(binding.root){


        init {
            binding.root.apply {  setOnClickListener {
                val position = adapterPosition
                if (position != currentSelectedColor && position != RecyclerView.NO_POSITION){
                    isSelected=true
                    notifyItemChanged(currentSelectedColor)
                    currentSelectedColor= position

                    listener.onColorItemClicked(colors[position])


                }

            }}
        }


        fun bind(item:Int){
            binding.apply {
                if(currentSelectedColor==-1){
                    root.isSelected=false
                }else{
                    root.isSelected = currentSelectedColor == adapterPosition
                }

                    colorItem.setBackgroundColor(ContextCompat.getColor(context,item))

            }


        }

    }

    override fun getItemCount()= colors.size


    interface  ColorRecyclerItemClickListener{
        fun onColorItemClicked(item: Int)
    }
}