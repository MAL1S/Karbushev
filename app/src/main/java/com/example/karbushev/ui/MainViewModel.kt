package com.example.karbushev.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.karbushev.data.Data
import com.example.karbushev.network.APIService
import com.example.karbushev.utils.LOG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val api = APIService.buildService()
    val data = MutableLiveData<Data>()
    val error = MutableLiveData<String>()

    fun getGifs(cat: String, page: Int) {
        val call: Call<Data> = api.getGifs(category = cat, pageNumber = page)

        call.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                Log.d(LOG, "====$cat, $page")
                error.postValue("success")
                data.postValue(response.body())
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                error.postValue("error")
                Log.d(LOG, "----$cat, $page")
            }
        })
    }
}