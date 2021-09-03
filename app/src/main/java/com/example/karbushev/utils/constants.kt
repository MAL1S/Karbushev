package com.example.karbushev.utils

import com.example.karbushev.ui.MainViewModel

const val LOG = "logd"
val TAB_TITLES = listOf<String>(
    "Последние",
    "Лучшие",
    "Горячие"
)
const val API = "http://developerslife.ru/"
const val LATEST = "latest"
const val HOT = "hot"
const val TOP = "top"
lateinit var VIEW_MODEL: MainViewModel
