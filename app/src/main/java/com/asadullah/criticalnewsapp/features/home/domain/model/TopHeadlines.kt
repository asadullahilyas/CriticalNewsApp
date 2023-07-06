package com.asadullah.criticalnewsapp.features.home.domain.model

import com.asadullah.criticalnewsapp.features.home.data.dto.ArticleResponse

data class TopHeadlines(

    val articles: List<ArticleResponse> = emptyList()

)
