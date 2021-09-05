package com.example.karbushev.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("result")
    @Expose
    val result: List<Gif>,

    @SerializedName("totalCount")
    @Expose
    val totalCount: Int
)