package com.asadullah.criticalnewsapp.api

import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.api.response.TopHeadlinesResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("top-headlines?sources=${BuildConfig.SOURCE}")
    suspend fun getTopHeadlines(): Response<TopHeadlinesResponse>
}