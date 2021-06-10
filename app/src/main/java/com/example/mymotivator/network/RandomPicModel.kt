package com.example.mymotivator.network

data class RandomPicModel(
    val urls:UnsplashPhotoUrls
) {
    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    )
}