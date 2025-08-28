package com.humanmusik.cleanhome.util

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow

interface SavedStateBehaviour<T: Any> {
    fun bind(state: MutableStateFlow<T>)
    fun restore(): T?
}

inline fun <reified T : Parcelable> saveState(
    savedStateHandle: SavedStateHandle,
): SavedStateBehaviour<T> = object : SavedStateBehaviour<T> {
    val SAVED_STATE_PROVIDER_KEY = "SavedStateProvider"
    val SAVED_STATE_KEY = "SavedState"

    override fun bind(state: MutableStateFlow<T>) {
        savedStateHandle.setSavedStateProvider(SAVED_STATE_PROVIDER_KEY) {
            bundleOf(SAVED_STATE_KEY to state)
        }
    }

    override fun restore(): T? {
        val bundle = savedStateHandle.get<Bundle>(SAVED_STATE_PROVIDER_KEY)
        return bundle?.getParcelableCompat(SAVED_STATE_KEY)
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)