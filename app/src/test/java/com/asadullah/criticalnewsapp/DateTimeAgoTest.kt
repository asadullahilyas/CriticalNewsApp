package com.asadullah.criticalnewsapp

import com.asadullah.criticalnewsapp.common.format
import com.asadullah.criticalnewsapp.features.home.domain.model.Article
import junit.framework.TestCase.assertEquals
import org.joda.time.DateTime
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DateTimeAgoTest {
    @Test
    fun zeroMinuteAgoTest() {
        val article = Article(
            publishedAt = DateTime.now()
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "0 minute ago")
    }

    @Test
    fun oneMinuteAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusMinutes(1)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "1 minute ago")
    }

    @Test
    fun moreThanOneMinuteAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusMinutes(20)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "20 minutes ago")
    }

    @Test
    fun oneHourAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusHours(1)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "1 hour ago")
    }

    @Test
    fun moreThanOneHourAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusHours(6)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "6 hours ago")
    }

    @Test
    fun moreThanTwentyFourHoursAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusHours(26)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "Yesterday")
    }

    @Test
    fun oneDayAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(1)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "Yesterday")
    }

    @Test
    fun moreThanOneDayAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(6)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "6 days ago")
    }

    @Test
    fun sevenDaysAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(7)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "1 week ago")
    }

    @Test
    fun moreThanSevenDaysAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(20)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "2 weeks ago")
    }

    @Test
    fun twentyTwoDaysAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(22)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "3 weeks ago")
    }

    @Test
    fun oneWeekAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusWeeks(1)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, "1 week ago")
    }

    @Test
    fun moreThanThirtyDaysAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusDays(31)
        )

        val result = article.publishedTimeAgo()

        assertEquals(result, article.publishedAt!!.format("MMM dd, YYYY"))
    }

    @Test
    fun twoMonthsAgoTest() {
        val article = Article(
            publishedAt = DateTime.now().minusMonths(2)
        )

        val result = article.publishedTimeAgo()

        assertNotEquals(result, "2 months ago")
    }
}