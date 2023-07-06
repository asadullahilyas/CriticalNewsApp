package com.asadullah.criticalnewsapp.features.home.domain.repo

import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import kotlinx.coroutines.flow.Flow

interface HomeRepo {
    fun getTopHeadlines(pageNumber: Int): Flow<Response<TopHeadlines>>
}