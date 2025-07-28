package com.humanmusik.cleanhome.presentation

import android.os.Parcel
import android.os.Parcelable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ViewState<out T> : Parcelable {
    class Loading<T> : ViewState<T>()

    data class Success<T>(val value: T) : ViewState<T>() {
        companion object : Parceler<Success<*>> {
            override fun Success<*>.write(parcel: Parcel, flags: Int) {
                val parcelClassName = value?.let {it::class.java.name } ?: "null"
                parcel.writeString("ViewState($parcelClassName)")
                when (parcelClassName) {
                    Unit::class.java.name,
                    "null"
                    -> parcel.writeString("ViewState($parcelClassName)")
                    else -> parcel.writeValue(value)
                }
            }

            override fun create(parcel: Parcel): Success<*> {
                val parcelClassName = parcel.readString()
                return if (parcelClassName == null) {
                    throw IllegalStateException("Unable to create ViewState.Success from parcel")
                } else {
                    when (val className = parcelClassName.removePrefix("AsyncState(").removeSuffix(")")) {
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

    data class Failure<T>(val throwable: Throwable) : ViewState<T>() {
        init {
            if (throwable is CancellationException) throw throwable
        }
    }


    override fun toString(): String {
        return when (this) {
            is Loading -> "ViewState.Loading"
            is Success -> "ViewState.Success($value)"
            is Failure -> "ViewState.Failure(${throwable::class.java.simpleName})"
        }
    }
}

fun <T> ViewState<T>.getOrNull(): T? {
    return when (this) {
        is ViewState.Success -> value
        else -> null
    }
}

inline fun <T> ViewState<T>.onLoading(block: () -> Unit): ViewState<T> {
    if (this is ViewState.Loading) block()
    return this
}

inline fun <T> Flow<ViewState<T>>.onLoading(crossinline block: () -> Unit): Flow<ViewState<T>> {
    return onEach { state ->
        state.onLoading(block)
    }
}

// Finish off extensions