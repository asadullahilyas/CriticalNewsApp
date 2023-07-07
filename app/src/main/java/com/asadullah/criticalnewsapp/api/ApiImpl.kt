package com.asadullah.criticalnewsapp.api

import com.asadullah.criticalnewsapp.api.response.ApiResponse
import com.asadullah.criticalnewsapp.features.home.data.dto.TopHeadlinesResponse
import javax.inject.Inject

class ApiImpl @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getModels(pageNumber: Int): ApiResponse<TopHeadlinesResponse> {
        return ApiResponse(apiInterface.getTopHeadlines(pageNumber))
    }
}