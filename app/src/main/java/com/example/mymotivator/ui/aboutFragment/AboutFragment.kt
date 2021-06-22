package com.example.mymotivator.ui.aboutFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mymotivator.R
import com.example.mymotivator.databinding.AboutFragmentLayoutBinding

class AboutFragment :Fragment(R.layout.about_fragment_layout) {

    private var _binding : AboutFragmentLayoutBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AboutFragmentLayoutBinding.bind(view)



        binding.apply {
            backButton.setOnClickListener {
                val action = AboutFragmentDirections.actionAboutFragmentToMainFragment()
                findNavController().navigate(action)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}