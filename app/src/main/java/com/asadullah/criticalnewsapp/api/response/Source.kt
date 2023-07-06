package com.asadullah.criticalnewsapp.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source (

  val id   : String? = null,
  val name : String? = null

)