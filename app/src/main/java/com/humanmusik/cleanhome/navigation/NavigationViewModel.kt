package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class NavigationViewModel : ViewModel() {
    private val mutableStateFlow: MutableStateFlow<List<NavKey>> = MutableStateFlow(listOf(TaskListNavKey))
    val backStack = mutableStateFlow.asStateFlow()

    fun push(navKey: NavKey) {
        val newBackStack = mutableStateFlow.value.toMutableList().apply {
            add(navKey)
        }
            .toList()
        mutableStateFlow.update { newBackStack }
    }

    fun pop() {
        val newBackStack = mutableStateFlow.value.toMutableList().apply {
            removeAt(lastIndex)
        }
            .toList()

        mutableStateFlow.update { newBackStack }
    }
}