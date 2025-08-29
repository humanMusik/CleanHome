package com.humanmusik.cleanhome.util

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow

interface SavedStateBehaviour<T: Any> {
    fun createSaveBehaviour(initialState: T): MutableStateFlow<T>
}

fun <T : Any> doNotSaveState(): SavedStateBehaviour<T> = object : SavedStateBehaviour<T> {
    override fun createSaveBehaviour(initialState: T): MutableStateFlow<T> {
        return MutableStateFlow(initialState)
    }
}

inline fun <reified T : Parcelable> saveState(
    savedStateHandle: SavedStateHandle,
): SavedStateBehaviour<T> = object : SavedStateBehaviour<T> {
    private val SAVED_STATE_KEY = "SavedState"

    override fun createSaveBehaviour(initialState: T): MutableStateFlow<T> {
        return savedStateHandle.getMutableStateFlow(SAVED_STATE_KEY, initialState)
    }
}
