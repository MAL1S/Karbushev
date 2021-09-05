package com.example.karbushev.network

import com.example.karbushev.data.Data
import com.example.karbushev.data.Gif
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/{category}/{pageNumber}?json=true")
    fun getGifs(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int
    ): Call<Data>

    @GET("/{gifNumber}?json=true")
    fun getRandomGif(
        @Path("gifNumber") gifNumber: Int
    ): Call<Gif>
}