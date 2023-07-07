package com.asadullah.criticalnewsapp.features.home.data.repo

import com.asadullah.criticalnewsapp.api.ApiImpl
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.data.dto.toTopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.repo.HomeRepo
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val apiImpl: ApiImpl
) : HomeRepo {
    override suspend fun getTopHeadlines(pageNumber: Int): Response<TopHeadlines> {
        val response = apiImpl.getModels(pageNumber)
        return if (response.isSuccessful()) {
            Response.Success(response.data?.toTopHeadlines())
        } else {
            Response.Error(response.error?.message ?: "Unknown error")
        }
    }
}