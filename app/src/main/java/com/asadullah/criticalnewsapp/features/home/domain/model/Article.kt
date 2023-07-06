package com.asadullah.criticalnewsapp.features.home.domain.model

data class Article(

    val source: Source? = Source(),
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null

)
