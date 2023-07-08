package com.asadullah.criticalnewsapp.features.home.data.repo

import com.asadullah.criticalnewsapp.api.ApiImpl
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.data.dto.toTopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.repo.HomeRepo
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val apiImpl: ApiImpl
) : HomeRepo {
    override suspend fun getTopHeadlines(pageNumber: Int): Response<TopHeadlines> {
        return try {
            val response = apiImpl.getModels(pageNumber)
            if (response.isSuccessful()) {
                Response.Success(response.data?.toTopHeadlines())
            } else {
                Response.Error(response.error?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Response.Error(e.message ?: "Unknown error")
        }
    }
}