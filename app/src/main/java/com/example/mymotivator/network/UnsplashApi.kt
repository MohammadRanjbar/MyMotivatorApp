package com.example.mymotivator.network

import retrofit2.http.GET
import retrofit2.http.Headers

interface UnsplashApi {

    companion object{
        const val API_KEY ="Ch0X0J1x-nbWV_zheSbbAusKNkSugcSLzFGjCjr0DYQ"
        const val BASE_URL ="https://api.unsplash.com/"
    }

    @Headers("Accept-Version: v1","Authorization: Client-ID $API_KEY")
    @GET("photos/random?orientation=portrait")
    suspend fun getRandomPicFromUnsplash():RandomPicModel



}