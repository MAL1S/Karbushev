package com.example.karbushev.network

import com.example.karbushev.data.Gif
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/{category}/{pageNumber}?json=true")
    suspend fun getGifs(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int
    ): Call<List<Gif>>
}