package com.example.karbushev.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.karbushev.data.Data
import com.example.karbushev.data.Gif
import com.example.karbushev.network.APIService
import com.example.karbushev.utils.ERROR
import com.example.karbushev.utils.LOG
import com.example.karbushev.utils.SUCCESS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val api = APIService.buildService()
    val data = MutableLiveData<Data>()
    val state = MutableLiveData<String>()
    val gif = MutableLiveData<Gif>()

    fun getGifs(cat: String, page: Int) {
        val call: Call<Data> = api.getGifs(category = cat, pageNumber = page)

        call.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                Log.d(LOG, "====$cat, $page")
                state.postValue(SUCCESS)
                data.postValue(response.body())
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                state.postValue(ERROR)
                Log.d(LOG, "----$cat, $page")
            }
        })
    }

    fun getRandomGif(gifNumber: Int) {
        val call: Call<Gif> = api.getRandomGif(gifNumber = gifNumber)

        call.enqueue(object : Callback<Gif> {
            override fun onResponse(call: Call<Gif>, response: Response<Gif>) {
                state.postValue(SUCCESS)
                gif.postValue(response.body())
            }

            override fun onFailure(call: Call<Gif>, t: Throwable) {
                state.postValue(ERROR)
            }
        })
    }

    fun raiseError() {
        state.postValue(ERROR)
    }
}