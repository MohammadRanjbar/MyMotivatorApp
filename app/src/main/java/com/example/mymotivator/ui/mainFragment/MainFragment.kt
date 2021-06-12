package com.example.mymotivator.ui.mainFragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mymotivator.R
import com.example.mymotivator.databinding.MainFragmentBinding
import com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters.FontRecyclerAdapter
import com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters.SettingRecyclerAdapter
import com.example.mymotivator.utils.SeperateStrings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), SettingRecyclerAdapter.SettingsClickedListener,FontRecyclerAdapter.FontRecyclerItemClickedListener{
    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    // this variable is as key to check which setting component visible if its open make it in visible
    // 0 = nothing is visible
    // 1 = font is visible
    // 2 = color
    // 3 = size
    private companion object var isSettingComponentOpen = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)


        //get first random pic
        viewModel.getRandomImg()

        //prepare list of sentences
        val rawSentences = resources.getString(R.string.sentences)
        val listOfSentenceAndAuther = SeperateStrings(rawSentences)


        //observe and change image if new random image requested
        viewModel.ImageResponseLiveData.observe(viewLifecycleOwner) {

            binding.apply {
                Glide.with(requireContext())
                    .load(it.urls.regular)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val randomIndex = Random.nextInt(0, listOfSentenceAndAuther.size)
                            sentenceTxt.text = listOfSentenceAndAuther[randomIndex].first
                            authorTxt.text = listOfSentenceAndAuther[randomIndex].second
                            txtContainer.visibility = View.VISIBLE

                            return false
                        }
                    })
                    .into(motivatorImg)
            }
        }


        // prepare  setting recycler layout manager and init  its adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val settingRecyclerAdapter = SettingRecyclerAdapter(this)
        // prepare font recycler layout manager and init  its adapter

        val fontLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val fontRecyclerAdapter = FontRecyclerAdapter(requireContext(),this)
        binding.apply {

            //get new image and sentence when referesh imgebtn was clicked
            referesh.setOnClickListener {
                viewModel.getRandomImg()
                txtContainer.visibility = View.INVISIBLE
            }

            // show preview of image when clicked on preview imageBtn
            preview.setOnClickListener {
                GlobalScope.launch {

                    launch(Dispatchers.Main) {
                        flow.visibility = View.INVISIBLE
                        uploadImg.visibility = View.INVISIBLE
                        menuImg.visibility = View.INVISIBLE
                    }
                    delay(3000)
                    launch(Dispatchers.Main) {
                        flow.visibility = View.VISIBLE
                        uploadImg.visibility = View.VISIBLE
                        menuImg.visibility = View.VISIBLE
                    }

                }
            }


            // init  setting recycler view
            settingRec.layoutManager = layoutManager
            settingRec.adapter = settingRecyclerAdapter

            // init font recycler view
            fontRecycler.layoutManager = fontLayoutManager
            fontRecycler.adapter = fontRecyclerAdapter


            setting.setOnClickListener {
                settingPanel.visibility = View.VISIBLE
            }

            settingCloseBtn.setOnClickListener {
                when (isSettingComponentOpen) {
                    0 -> {
                        settingPanel.visibility = View.INVISIBLE
                    }
                    1 -> {
                        fontRecycler.visibility = View.INVISIBLE
                        isSettingComponentOpen = 0
                    }
                }
            }
        }


    }

    override fun onSettingsItemClicked(item: String) {
        when (item) {
            "font" -> {
                binding.fontRecycler.visibility = View.VISIBLE
                isSettingComponentOpen = 1
            }
            "color" -> {
            }
            "size" -> {
            }
        }
    }

    override fun onFontRecyclerItemClicked(font: String) {
        binding.apply {
            when (font) {
                "Stint Ultra" -> {
                   sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.stint_ultra_condensed_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.stint_ultra_condensed_regular)
                }
                "Abril Fat face" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.abril_fatface_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.abril_fatface_regular)

                }
                "Dancing Script" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.dancingscript_variable_font_wght)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.dancingscript_variable_font_wght)
                }
                "Hammer Smith" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.hammersmith_one_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.hammersmith_one_regular)
                }
                "Indie flower" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.indie_flower_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.indie_flower_regular)
                }
                "Lobster" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.lobster_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.lobster_regular)
                }
                "Odibee sans" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.odibee_sans_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.odibee_sans_regular)
                }
                "Pacifico" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.pacifico_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.pacifico_regular)
                }
                "Shadows Into Lights" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.shadows_into_light_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.stint_ultra_condensed_regular)
                }
                "Viaoda Libre" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.viaoda_libre_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(requireContext(),R.font.viaoda_libre_regular)
                }

            }
        }
    }
}