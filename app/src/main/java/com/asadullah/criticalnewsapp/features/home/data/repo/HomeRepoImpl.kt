package com.asadullah.criticalnewsapp.features.home.data.repo

import com.asadullah.criticalnewsapp.api.ApiImpl
import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.data.dto.toTopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.repo.HomeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepoImpl @Inject constructor(
    private val apiImpl: ApiImpl
) : HomeRepo {
    override fun getTopHeadlines(pageNumber: Int): Flow<Response<TopHeadlines>> {
        return flow {

            emit(Response.Loading())

            val response = apiImpl.getModels(pageNumber)
            if (response.isSuccessful()) {
                emit(Response.Success(response.data?.toTopHeadlines()))
            } else {
                emit(Response.Error(response.error?.message ?: "Unknown error"))
            }
        }
    }
}