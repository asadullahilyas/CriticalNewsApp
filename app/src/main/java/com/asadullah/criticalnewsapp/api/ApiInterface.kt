package com.asadullah.criticalnewsapp.api

import com.asadullah.criticalnewsapp.BuildConfig
import com.asadullah.criticalnewsapp.features.home.data.dto.TopHeadlinesResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("top-headlines?sources=${BuildConfig.SOURCE_ID}&apiKey=${BuildConfig.NEWS_API_SECRET_KEY}&pageSize=4")
    suspend fun getTopHeadlines(@Query("page") pageNumber: Int): Response<TopHeadlinesResponse>
}