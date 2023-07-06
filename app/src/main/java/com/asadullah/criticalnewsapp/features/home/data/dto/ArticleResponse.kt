package com.asadullah.criticalnewsapp.features.home.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticleResponse (

    val source      : SourceResponse? = SourceResponse(),
    val author      : String? = null,
    val title       : String? = null,
    val description : String? = null,
    val url         : String? = null,
    val urlToImage  : String? = null,
    val publishedAt : String? = null,
    val content     : String? = null

)