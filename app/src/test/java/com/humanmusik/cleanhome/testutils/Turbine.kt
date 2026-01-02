package com.humanmusik.cleanhome.testutils

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.seconds

suspend fun <T> Flow<T>.test(validate: suspend TurbineTestContext<T>.() -> Unit) =
    this.test(timeout = 10.seconds, validate = validate)