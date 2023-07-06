package com.asadullah.criticalnewsapp.features.home.data.dto

import com.asadullah.criticalnewsapp.features.home.domain.model.TopHeadlines
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopHeadlinesResponse (

    val status       : String?        = null,
    val totalResults : Int?           = null,
    val articles     : List<ArticleResponse> = emptyList()

)

fun TopHeadlinesResponse.toTopHeadlines(): TopHeadlines {
    return TopHeadlines(
        articles = this.articles
    )
}