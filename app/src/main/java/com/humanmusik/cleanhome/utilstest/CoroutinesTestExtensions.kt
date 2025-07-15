package com.humanmusik.cleanhome.utilstest

import kotlinx.coroutines.test.TestScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.seconds

fun runTest(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: kotlin.time.Duration = 10.seconds,
    testBody: suspend TestScope.() -> Unit,
) =
    kotlinx.coroutines.test.runTest(
        context = context,
        timeout = timeout,
        testBody = testBody,
    )