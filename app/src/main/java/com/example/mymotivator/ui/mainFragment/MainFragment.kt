package com.example.mymotivator.ui.mainFragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mymotivator.R
import com.example.mymotivator.databinding.MainFragmentBinding
import com.example.mymotivator.ui.utils.SeperateStrings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {
    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding : MainFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding = MainFragmentBinding.bind(view)
        //get first random pic
        viewModel.getRandomImg()

//prepare list of sentences
        val rawSentences = resources.getString(R.string.sentences)
        val listOfSentenceAndAuther= SeperateStrings(rawSentences)

        //observe and change image if new random image requested
        viewModel.ImageResponseLiveData.observe(viewLifecycleOwner){

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
                           val randomIndex= Random.nextInt(0,listOfSentenceAndAuther.size)
                            sentenceTxt.text = listOfSentenceAndAuther[randomIndex].first
                            authorTxt.text = listOfSentenceAndAuther[randomIndex].second
                            txtContainer.visibility = View.VISIBLE

                            return false
                        }
                    })
                    .into(motivatorImg)
            }
        }


        // prepare recycler layout manager and init  its adapter
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val settingRecyclerAdapter = SettingRecyclerAdapter()
        binding.apply {

            //get new image and sentence when referesh imgebtn was clicked
            referesh.setOnClickListener {
                viewModel.getRandomImg()
                txtContainer.visibility = View.INVISIBLE
            }

        // show preview of image when clicked on preview imageBtn
            preview.setOnClickListener {
                GlobalScope.launch {

                    launch (Dispatchers.Main) {
                        flow.visibility = View.INVISIBLE
                        uploadImg.visibility = View.INVISIBLE
                        menuImg.visibility =View.INVISIBLE
                    }
                    delay(3000)
                    launch (Dispatchers.Main) {
                        flow.visibility = View.VISIBLE
                        uploadImg.visibility = View.VISIBLE
                        menuImg.visibility = View.VISIBLE
                    }

                }
            }


            // init recycler view
            settingRec.layoutManager = layoutManager
            settingRec.adapter = settingRecyclerAdapter


            setting.setOnClickListener {
                settingPanel.visibility = View.VISIBLE
            }

            settingCloseBtn.setOnClickListener {
                settingPanel.visibility = View.INVISIBLE
            }
        }


    }
}