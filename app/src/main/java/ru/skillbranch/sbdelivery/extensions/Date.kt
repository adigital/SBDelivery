package ru.skillbranch.sbdelivery.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.shortFormat(): String {
    val pattern = "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

fun millisToDate(millis: Long): String {
    return SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(Date(millis))
}