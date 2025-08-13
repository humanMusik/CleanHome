package com.humanmusik.cleanhome.presentation

import android.os.Parcel
import android.os.Parcelable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Parcelize
sealed class FlowState<out T> : Parcelable {
    class Loading<T> : FlowState<T>()

    data class Success<T>(val value: T) : FlowState<T>() {
        companion object : Parceler<Success<*>> {
            override fun Success<*>.write(parcel: Parcel, flags: Int) {
                val parcelClassName = value?.let {it::class.java.name } ?: "null"
                parcel.writeString("FlowState($parcelClassName)")
                when (parcelClassName) {
                    Unit::class.java.name,
                    "null"
                    -> parcel.writeString("FlowState($parcelClassName)")
                    else -> parcel.writeValue(value)
                }
            }

            override fun create(parcel: Parcel): Success<*> {
                val parcelClassName = parcel.readString()
                return if (parcelClassName == null) {
                    throw IllegalStateException("Unable to create FlowState.Success from parcel")
                } else {
                    when (val className = parcelClassName.removePrefix("FlowState(").removeSuffix(")")) {
                        "null" -> Success(null)
                        Unit::class.java.name -> Success(Unit)
                        else -> {
                            Success(parcel.readValue(Class.forName(className).classLoader))
                        }
                    }
                }
            }
        }
    }

    data class Failure<T>(val throwable: Throwable) : FlowState<T>() {
        init {
            if (throwable is CancellationException) throw throwable
        }
    }


    override fun toString(): String {
        return when (this) {
            is Loading -> "FlowState.Loading"
            is Success -> "FlowState.Success($value)"
            is Failure -> "FlowState.Failure(${throwable::class.java.simpleName})"
        }
    }

    companion object
}

@OptIn(ExperimentalContracts::class)
fun <T> FlowState<T>.getOrNull(): T? {
    contract { returnsNotNull() implies (this@getOrNull is FlowState.Success) }
    return when (this) {
        is FlowState.Success -> value
        else -> null
    }
}

@OptIn(ExperimentalContracts::class)
fun <T> FlowState<T>.isLoading(): Boolean {
    contract { returns(true) implies (this@isLoading is FlowState.Loading) }
    return this is FlowState.Loading
}

@OptIn(ExperimentalContracts::class)
fun <T> FlowState<T>.isSuccess(): Boolean {
    contract { returns(true) implies (this@isSuccess is FlowState.Success) }
    return this is FlowState.Success
}

@OptIn(ExperimentalContracts::class)
fun <T> FlowState<T>.isFailure(): Boolean {
    contract { returns(true) implies (this@isFailure is FlowState.Failure) }
    return this is FlowState.Failure
}

inline fun <T> FlowState<T>.onLoading(block: () -> Unit): FlowState<T> {
    if (isLoading()) block()
    return this
}

inline fun <T> FlowState<T>.onSuccess(block: (T) -> Unit): FlowState<T> {
    if (isSuccess()) block(value)
    return this
}

inline fun <T> FlowState<T>.onFailure(block: () -> Unit): FlowState<T> {
    if (isFailure()) block()
    return this
}

inline fun <T> Flow<FlowState<T>>.onLoading(crossinline block: () -> Unit): Flow<FlowState<T>> {
    return onEach { state ->
        state.onLoading(block)
    }
}

inline fun <T> Flow<FlowState<T>>.onSuccess(crossinline block: (T) -> Unit): Flow<FlowState<T>> {
    return onEach { state ->
        state.onSuccess(block)
    }
}

inline fun <T> Flow<FlowState<T>>.onFailure(crossinline block: () -> Unit): Flow<FlowState<T>> {
    return onEach { state ->
        state.onFailure(block)
    }
}

fun <T> FlowState.Companion.fromSuspendingFunc(
   block: suspend () -> T,
): Flow<FlowState<T>> {
    return flow {
        emit(FlowState.Loading())
        emit(FlowState.Success(value = block()))
    }
        .catch { throwable -> emit(FlowState.Failure(throwable)) }
}

fun <T> (suspend () -> T).asFlowState(): Flow<FlowState<T>> {
    return FlowState.fromSuspendingFunc(this)
}

fun <T> FlowState.Companion.fromFlow(
    flow: Flow<T>,
): Flow<FlowState<T>> {
    return flow
        .map<T, FlowState<T>> { FlowState.Success(value = it) }
        .catch { throwable -> emit(FlowState.Failure(throwable)) }
        .onStart { emit(FlowState.Loading()) }
}

fun <T> Flow<T>.asFlowState(): Flow<FlowState<T>> {
    return FlowState.fromFlow(this)
}

fun <T, R> FlowState<T>.map(
    block: (T) -> R,
): FlowState<R> {
    return when (this) {
        is FlowState.Success -> FlowState.Success(block(value))
        else -> this as FlowState<R>
    }
}
// Finish off extensions