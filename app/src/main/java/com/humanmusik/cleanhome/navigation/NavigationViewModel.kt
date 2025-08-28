package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.util.saveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val savedStateBehaviour = saveState<BackStack>(savedStateHandle)
    private val savedState = savedStateBehaviour.restore()
    private val initialState = BackStack(navKeys = mutableStateListOf(TaskListNavKey))
    private val mutableStateFlow =
        MutableStateFlow(savedState ?: initialState).also { stateFlow ->
            savedStateBehaviour.bind(stateFlow)
        }

    val backStack = mutableStateFlow.asStateFlow()

    fun push(navKey: CustomNavKey) {
        mutableStateFlow.update {
            it.copy(navKeys = it.navKeys.addAndReturn(navKey))
        }
    }

    fun pop() {
        if (mutableStateFlow.value.navKeys.size > 1) {
            mutableStateFlow.update {
                it.copy(navKeys = it.navKeys.apply { removeLastOrNull() })
            }
        }
    }
}

fun <T> SnapshotStateList<T>.addAndReturn(element: T): SnapshotStateList<T> {
    add(element)
    return this
}