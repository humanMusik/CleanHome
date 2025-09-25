package com.humanmusik.cleanhome.utilstest

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun runTest(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 10.seconds,
    testBody: suspend TestScope.() -> Unit,
) =
    kotlinx.coroutines.test.runTest(
        context = context,
        timeout = timeout,
        testBody = testBody,
    )

fun runCancellingTest(
    testBody: suspend TestScope.() -> Unit,
) {
    try {
        runTest {
            testBody(this)
            cancel(cause = TestJobCancellationException())
        }
    } catch (e: TestJobCancellationException) {
        // Do nothing
    }
}

private class TestJobCancellationException : CancellationException()
