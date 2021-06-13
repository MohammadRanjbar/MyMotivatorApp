package com.example.mymotivator.ui.mainFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
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
import com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters.ColorRecyclerAdapter
import com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters.FontRecyclerAdapter
import com.example.mymotivator.ui.mainFragment.mainFragmentRecyclerAdapters.SettingRecyclerAdapter
import com.example.mymotivator.utils.CheckConnectionLiveData
import com.example.mymotivator.utils.LoadState
import com.example.mymotivator.utils.SeperateStrings
import com.example.mymotivator.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment),
    SettingRecyclerAdapter.SettingsClickedListener,
    FontRecyclerAdapter.FontRecyclerItemClickedListener,
    ColorRecyclerAdapter.ColorRecyclerItemClickListener {
    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding
    private lateinit var alertDialog: AlertDialog

    // this variable is as key to check which setting component visible if its open make it in visible
    // 0 = nothing is visible
    // 1 = font is visible
    // 2 = color
    // 3 = size

    private var isSettingComponentOpen = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

         setUpShowConnectionAndLoadingAnimation()
        //check internet connection and request  random image
        CheckConnectionLiveData(requireContext()).observe(viewLifecycleOwner) {
            if (it) {
                binding.noConnection.visibility = View.INVISIBLE
                viewModel.getRandomImg()
            } else {
                binding.noConnection.visibility = View.VISIBLE
            }

        }

        viewModel.LoadingStateLiveData.observe(viewLifecycleOwner) { loadState->

            when (loadState) {
                LoadState.LOADED -> {
                    binding.noConnection.visibility = View.INVISIBLE
                    alertDialog.dismiss()

                }
                LoadState.LOADING -> {
                    alertDialog.show()
                }
                LoadState.ERROR -> {
                  alertDialog.dismiss()
                    Log.i("MyMotivator", "onViewCreated: Error ")


                }
                else->{}

            }.exhaustive

        }

        //prepare list of sentences
        val rawSentences = resources.getString(R.string.sentences)
        val listOfSentenceAndAuthor = SeperateStrings(rawSentences)


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
                            Log.i("MyMotivator", "onViewCreated: Glide Error ")

                            viewModel.LoadingStateLiveData.postValue(LoadState.ERROR)

                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            val randomIndex = Random.nextInt(0, listOfSentenceAndAuthor.size)
                            sentenceTxt.text = listOfSentenceAndAuthor[randomIndex].first
                            authorTxt.text = listOfSentenceAndAuthor[randomIndex].second
                            txtContainer.visibility = View.VISIBLE
                            viewModel.LoadingStateLiveData.postValue(LoadState.LOADED)

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
        val fontRecyclerAdapter = FontRecyclerAdapter(requireContext(), this)

        // prepare color recycler layout manager and init  its adapter
        val colorLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val colorRecyclerAdapter = ColorRecyclerAdapter(requireContext(), this)

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

            // init color recycler view
            colorRecycler.layoutManager = colorLayoutManager
            colorRecycler.adapter = colorRecyclerAdapter


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
                    2 -> {
                        colorRecycler.visibility = View.INVISIBLE
                        isSettingComponentOpen = 0
                    }
                    3 -> {
                        sizePanel.visibility = View.INVISIBLE
                        isSettingComponentOpen = 0
                    }
                }
            }

            increaseSizeBtn.setOnClickListener {
                var txtSize = sizeTxt.text.toString().toFloat()
                if (txtSize <= 24) {
                    var size = ++txtSize
                    sizeTxt.text = size.toInt().toString()
                    sentenceTxt.textSize = ++size

                }
            }
            decreaseSizeBtn.setOnClickListener {
                var txtSize = sizeTxt.text.toString().toFloat()
                if (txtSize >= 8) {
                    var size = --txtSize
                    sizeTxt.text = size.toInt().toString()
                    sentenceTxt.textSize = --size
                }
            }
        }


    }

    //implementing setting recycler view click listener
    override fun onSettingsItemClicked(item: String) {
        when (item) {
            "font" -> {
                binding.apply {
                    colorRecycler.visibility = View.INVISIBLE
                    sizePanel.visibility = View.INVISIBLE
                    fontRecycler.visibility = View.VISIBLE

                }
                isSettingComponentOpen = 1
            }
            "color" -> {
                binding.apply {
                    fontRecycler.visibility = View.INVISIBLE
                    sizePanel.visibility = View.INVISIBLE
                    colorRecycler.visibility = View.VISIBLE
                }
                isSettingComponentOpen = 2
            }
            "size" -> {
                binding.apply {
                    fontRecycler.visibility = View.INVISIBLE
                    colorRecycler.visibility = View.INVISIBLE
                    sizePanel.visibility = View.VISIBLE

                }
                isSettingComponentOpen = 3
            }
        }
    }


    //implementing font recycler view click listener
    override fun onFontRecyclerItemClicked(font: String) {
        binding.apply {
            when (font) {
                "Stint Ultra" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.stint_ultra_condensed_regular
                    )
                    authorTxt.typeface = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.stint_ultra_condensed_regular
                    )
                }
                "Abril Fat face" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.abril_fatface_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.abril_fatface_regular)

                }
                "Dancing Script" -> {
                    sentenceTxt.typeface = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.dancingscript_variable_font_wght
                    )
                    authorTxt.typeface = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.dancingscript_variable_font_wght
                    )
                }
                "Hammer Smith" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.hammersmith_one_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.hammersmith_one_regular)
                }
                "Indie flower" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.indie_flower_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.indie_flower_regular)
                }
                "Lobster" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.lobster_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.lobster_regular)
                }
                "Odibee sans" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.odibee_sans_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.odibee_sans_regular)
                }
                "Pacifico" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.pacifico_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.pacifico_regular)
                }
                "Shadows Into Lights" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.shadows_into_light_regular)
                    authorTxt.typeface = ResourcesCompat.getFont(
                        requireContext(),
                        R.font.stint_ultra_condensed_regular
                    )
                }
                "Viaoda Libre" -> {
                    sentenceTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.viaoda_libre_regular)
                    authorTxt.typeface =
                        ResourcesCompat.getFont(requireContext(), R.font.viaoda_libre_regular)
                }

            }
        }
    }

    override fun onColorItemClicked(item: Int) {
        binding.apply {
            sentenceTxt.setTextColor(ContextCompat.getColor(requireContext(), item))
            authorTxt.setTextColor(ContextCompat.getColor(requireContext(), item))
        }
    }
    private fun setUpShowConnectionAndLoadingAnimation() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val viewGroup: ViewGroup = activity?.findViewById(android.R.id.content) as ViewGroup
        val dialogView: View =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.loading_dialog_layout, viewGroup, false)


        dialogView.setBackgroundColor(Color.TRANSPARENT)
        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    }
}