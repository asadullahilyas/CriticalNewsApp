package com.asadullah.criticalnewsapp.api

import com.asadullah.criticalnewsapp.api.response.Response
import com.asadullah.criticalnewsapp.features.home.data.dto.TopHeadlinesResponse
import javax.inject.Inject

class ApiImpl @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getModels(pageNumber: Int): Response<TopHeadlinesResponse> {
        return Response(apiInterface.getTopHeadlines(pageNumber))
    }
}