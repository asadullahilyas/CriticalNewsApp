package com.asadullah.criticalnewsapp.features.home.domain.repo

import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines

interface HomeRepo {
    suspend fun getTopHeadlines(pageNumber: Int): Response<TopHeadlines>
}