package com.example.mymotivator.ui.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymotivator.network.RandomPicModel
import com.example.mymotivator.network.UnsplashApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject
constructor(
    private val unsplashApi: UnsplashApi
):ViewModel() {

    private val _ImageResponseLiveData = MutableLiveData<RandomPicModel>()


    fun getRandomImg(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = unsplashApi.getRandomPicFromUnsplash()
                _ImageResponseLiveData.postValue(result)
            }catch (e:Exception){

            }
        }
    }

    val ImageResponseLiveData:LiveData<RandomPicModel> = _ImageResponseLiveData
}