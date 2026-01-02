package com.humanmusik.cleanhome.testutils

import com.google.common.truth.Truth.assertThat
import kotlin.reflect.KClass

infix fun <T> T.assertIsEqualTo(expected: T) {
    assertThat(this).isEqualTo(expected)
}

infix fun <T> T.assertIsNotEqualTo(expected: T) {
    assertThat(this).isNotEqualTo(expected)
}

infix fun Any?.assertIsInstanceOf(expected: KClass<*>) {
    assertThat(this).isInstanceOf(expected.java)
}

inline fun <reified T> Any?.assertIsInstanceOf() {
    assertThat(this).isInstanceOf(T::class.java)
}

inline fun <reified T> Any?.assertIsInstanceOf(
    block: T.() -> Unit,
) {
    assertThat(this).isInstanceOf(T::class.java)
    (this as T).block()
}

infix fun <T> Iterable<T>.assertContains(expected: T) {
    assertThat(this).contains(expected)
}

infix fun <T> Iterable<T>.assertContainsExactlyElementsIn(expected: Iterable<T>) {
    assertThat(this).containsExactlyElementsIn(expected)
}