package com.example.mymotivator.di

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.mymotivator.network.UnsplashApi
import com.example.mymotivator.ui.MainActivity
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit):UnsplashApi =retrofit.create(UnsplashApi::class.java)




}
