package com.example.karbushev.data

import com.example.karbushev.network.APIService

class Repository(private val apiService: APIService) {

    //suspend fun getGifs(cat: String, page: Int) = apiService.getGifs(cat, page)

   /* companion object {

        private var repository: Repository? = null
        private var apiService: APIService? = null

        @Synchronized
        fun getInstance(): Repository {
            return if (repository == null) {
                repository = Repository()
                repository as Repository
            } else repository as Repository
        }
    }*/
}