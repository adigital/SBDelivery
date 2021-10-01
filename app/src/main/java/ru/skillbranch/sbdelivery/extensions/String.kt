package ru.skillbranch.sbdelivery.extensions

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidLettersOnly(): Boolean {
    val pattern = Pattern.compile("""^[а-яёa-z]+""", CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(this)

    return matcher.matches()
}
