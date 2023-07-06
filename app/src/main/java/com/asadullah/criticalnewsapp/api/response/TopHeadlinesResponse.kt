package com.asadullah.criticalnewsapp.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopHeadlinesResponse (

  val status       : String?        = null,
  val totalResults : Int?           = null,
  val articles     : List<Articles> = arrayListOf()

)