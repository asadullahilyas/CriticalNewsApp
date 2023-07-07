package com.asadullah.criticalnewsapp.common

fun String?.isNullOrEmptyOrBlank(): Boolean {
    return this.isNullOrEmpty() || this.isBlank()
}

fun String?.isNeitherNullNorEmptyNorBlank(): Boolean {
    return !isNullOrEmptyOrBlank()
}