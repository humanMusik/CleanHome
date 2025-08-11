package com.humanmusik.cleanhome.utilstest

import com.google.common.truth.Truth.assertThat

infix fun <T> T.assertIsEqualTo(expected: T) {
    assertThat(this).isEqualTo(expected)
}

infix fun <T> T.assertIsNotEqualTo(expected: T) {
    assertThat(this).isNotEqualTo(expected)
}

infix fun <T> Iterable<T>.assertContains(expected: T) {
    assertThat(this).contains(expected)
}

infix fun <T> Iterable<T>.assertContainsExactlyElementsIn(expected: Iterable<T>) {
    assertThat(this).containsExactlyElementsIn(expected)
}