package com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mymotivator.R
import com.example.mymotivator.databinding.FontRecyclerItemBinding
import com.example.mymotivator.utils.exhaustive

class FontRecyclerAdapter(private var context: Context,private var listener:FontRecyclerItemClickedListener) : RecyclerView.Adapter<FontRecyclerAdapter.ViewHolder>() {

    private val fontsNames = listOf(
        "Stint Ultra",
        "Abril Fat face",
        "Dancing Script",
        "Hammer Smith",
        "Indie flower",
        "Lobster",
        "Odibee sans",
        "Pacifico",
        "Shadows Into Lights",
        "Viaoda Libre"

    )

    private var selectedFontPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FontRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = fontsNames[position]
        holder.bind(item)

    }

    inner class ViewHolder(private var binding: FontRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.apply {
                setOnClickListener{
                    val position = adapterPosition
                    if (selectedFontPosition != position){
                       binding.fontNameTxt.setTextColor(Color.WHITE)
                        isSelected = true
                        notifyItemChanged(selectedFontPosition)
                        selectedFontPosition = adapterPosition

                    }
                    if(position != RecyclerView.NO_POSITION){
                        listener.onFontRecyclerItemClicked(fontsNames[position])
                    }

                }
            }
        }


        fun bind(font: String) {
            binding.apply {
                if (selectedFontPosition == -1) {
                    fontNameTxt.setTextColor(Color.BLACK)
                    root.isSelected = false
                } else {
                    if (selectedFontPosition == adapterPosition) {
                        fontNameTxt.setTextColor(Color.WHITE)
                        root.isSelected = true
                    } else {
                        fontNameTxt.setTextColor(Color.BLACK)
                        root.isSelected = false
                    }
                }


                when (font) {
                    "Stint Ultra" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.stint_ultra_condensed_regular)
                        fontNameTxt.text = font

                    }
                    "Abril Fat face" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.abril_fatface_regular)
                        fontNameTxt.text = font

                    }
                    "Dancing Script" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.dancingscript_variable_font_wght)
                        fontNameTxt.text = font
                    }
                    "Hammer Smith" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.hammersmith_one_regular)
                        fontNameTxt.text = font
                    }
                    "Indie flower" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.indie_flower_regular)
                        fontNameTxt.text = font
                    }
                    "Lobster" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.lobster_regular)
                        fontNameTxt.text = font
                    }
                    "Odibee sans" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.odibee_sans_regular)
                        fontNameTxt.text = font
                    }
                    "Pacifico" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.pacifico_regular)
                        fontNameTxt.text = font
                    }
                    "Shadows Into Lights" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.shadows_into_light_regular)
                        fontNameTxt.text = font
                    }
                    "Viaoda Libre" -> {
                        fontNameTxt.typeface = ResourcesCompat.getFont(context, R.font.viaoda_libre_regular)
                        fontNameTxt.text = font
                    }

                }
            }

        }


    }

    override fun getItemCount() = fontsNames.size

    interface  FontRecyclerItemClickedListener{
        fun onFontRecyclerItemClicked(font:String)
    }
}