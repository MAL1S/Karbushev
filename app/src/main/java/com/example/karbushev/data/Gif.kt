package com.example.karbushev.data

import com.google.gson.annotations.SerializedName

data class Gif(
    @SerializedName("gifURL") val url: String,
    @SerializedName("description") val desc: String
)