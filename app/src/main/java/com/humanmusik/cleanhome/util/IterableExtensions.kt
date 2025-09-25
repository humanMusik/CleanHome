package com.humanmusik.cleanhome.util

import kotlin.jvm.Throws

@Throws(IllegalStateException::class)
fun <T> Iterable<T>.findOrThrow(predicate: (T) -> Boolean): T {
    return find(predicate) ?: throw IllegalStateException("Unable to find element in Collection")
}