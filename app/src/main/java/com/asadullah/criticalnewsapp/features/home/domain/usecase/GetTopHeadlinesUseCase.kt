package com.asadullah.criticalnewsapp.features.home.domain.usecase

import com.asadullah.criticalnewsapp.common.Response
import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.asadullah.criticalnewsapp.features.home.domain.repo.HomeRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val homeRepo: HomeRepo
) {
    operator fun invoke(pageNumber: Int): Flow<Response<TopHeadlines>> {
        return homeRepo.getTopHeadlines(pageNumber)
    }
}