package com.asadullah.criticalnewsapp.features.home.domain.model

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Weeks

data class Article(

    val source: Source? = Source(),
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: DateTime? = null,
    val content: String? = null

) {

    fun publishedTimeAgo(): String? {

        if (publishedAt == null) return null

        val now = DateTime.now()
        val daysBetween = Days.daysBetween(publishedAt, now).days
        return if (daysBetween > 30) {
            publishedAt.toString("MMM dd, YYYY")
        } else if (daysBetween > 7) {
            val weeksBetween = Weeks.weeksBetween(publishedAt, now).weeks
            "$weeksBetween week ${if (weeksBetween > 1) "s" else ""} ago"
        } else if (daysBetween > 1) {
            "$daysBetween days ago"
        } else if (daysBetween == 1) {
            "Yesterday"
        } else {
            val hoursBetween = Hours.hoursBetween(publishedAt, now).hours
            if (hoursBetween > 0) {
                "$hoursBetween hour${if (hoursBetween > 1) "s" else ""} ago"
            } else {
                val minutesBetween = Minutes.minutesBetween(publishedAt, now).minutes
                "$minutesBetween minute${if (minutesBetween > 1) "s" else ""} ago"
            }
        }
    }
}