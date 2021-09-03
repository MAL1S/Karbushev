package com.example.karbushev.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karbushev.data.Data
import com.example.karbushev.network.APIService
import com.example.karbushev.utils.LATEST
import com.example.karbushev.utils.LOG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val api = APIService.buildService()
    val data = MutableLiveData<Data>()

    fun getGifs(cat: String, page: Int) {
        val call: Call<Data> = api.getGifs(category = cat, pageNumber = page)

        call.enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                data.postValue(response.body())
                Log.d(LOG, "refreshed")
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Log.d(LOG, t.message.toString())
            }
        })
    }
}