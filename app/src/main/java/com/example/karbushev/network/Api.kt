package com.example.karbushev.network

import com.example.karbushev.data.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/{category}/{pageNumber}?json=true")
    fun getGifs(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int
    ): Call<Data>

    @GET("/{category}/{pageNumber}?json=true")
    fun getRandomGif(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int
    ): Call<Data>
}