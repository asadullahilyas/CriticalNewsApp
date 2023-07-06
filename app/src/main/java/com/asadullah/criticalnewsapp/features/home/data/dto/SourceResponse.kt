package com.asadullah.criticalnewsapp.features.home.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceResponse (

    val id   : String? = null,
    val name : String? = null

)
