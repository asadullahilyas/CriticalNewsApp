package com.asadullah.criticalnewsapp.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun String?.isNullOrEmptyOrBlank(): Boolean {
    return this.isNullOrEmpty() || this.isBlank()
}

fun String?.isNeitherNullNorEmptyNorBlank(): Boolean {
    return !isNullOrEmptyOrBlank()
}

fun DateTime.format(format: String): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(this.toDate())
}

fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        this@mutableStateIn.collect(flow)
    }

    return flow
}