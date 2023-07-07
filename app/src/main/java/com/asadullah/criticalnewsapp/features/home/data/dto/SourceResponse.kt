package com.asadullah.criticalnewsapp.features.home.data.dto

import com.asadullah.criticalnewsapp.features.home.domain.model.Source
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceResponse (

    val id   : String? = null,
    val name : String? = null

)

fun SourceResponse.toSource(): Source {
    return Source(id, name)
}