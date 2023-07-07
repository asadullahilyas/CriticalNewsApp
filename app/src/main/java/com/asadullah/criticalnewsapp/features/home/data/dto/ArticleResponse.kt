package com.asadullah.criticalnewsapp.features.home.data.dto

import com.asadullah.criticalnewsapp.common.SDKHelper
import com.asadullah.criticalnewsapp.common.isNullOrEmptyOrBlank
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import com.squareup.moshi.JsonClass
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter

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

fun ArticleResponse.toArticle(): Article {
    return Article(
        source = source?.toSource(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = if (SDKHelper.hasO()) convertStringIntoDate(publishedAt) else DateTime.now(),
        content = content
    )
}

private fun convertStringIntoDate(dateTimeStr: String?): DateTime? {

    if (dateTimeStr.isNullOrEmptyOrBlank()) return null

    val dateTimeArray = dateTimeStr!!.split("T")

    val dateStr = dateTimeArray[0]
    val timeStr = dateTimeArray[1]

    val timeArray = timeStr.split(":")

    val improvedTimeStr = "${timeArray[0]}:${timeArray[1]}"

    val improvedDateTimeStr = "${dateStr}T${improvedTimeStr}"

    val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm")
    return DateTime.parse(improvedDateTimeStr, formatter)
}