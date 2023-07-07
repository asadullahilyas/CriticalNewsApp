package com.asadullah.criticalnewsapp.common

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object SDKHelper {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
    fun hasN1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    fun hasO() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
    fun hasO1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
    fun hasP() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun hasQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
    fun hasR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun hasS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}