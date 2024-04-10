package com.example.bookingchallenge.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.sql.Date
import java.util.Locale


fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun convertDateLongToString(longDate: Long, pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(longDate))
}

fun calculateNights(checkInLong: Long, checkOutLong: Long): Int {
    return ((checkOutLong - checkInLong) / (1000 * 60 * 60 * 24)).toInt()
}

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()