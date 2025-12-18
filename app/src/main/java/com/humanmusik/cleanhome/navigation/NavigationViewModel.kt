package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.util.saveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val backStack = savedStateFlow(
        savedStateBehaviour = saveState(savedStateHandle),
        initialState = BackStack(navKeys = mutableStateListOf(AuthenticationNavKey.LoadingUp)),
    )

    fun push(navKey: CustomNavKey) {
        backStack.update {
            it.copy(navKeys = it.navKeys.addAndReturn(navKey))
        }
    }

    fun pop() {
        if (backStack.value.navKeys.size > 1) {
            backStack.update {
                it.copy(navKeys = it.navKeys.apply { removeAt(it.navKeys.lastIndex) })
            }
        }
    }

    fun popUntil(navKey: CustomNavKey) {
        if (backStack.value.navKeys.contains(navKey)) {
            while (backStack.value.navKeys.last() != navKey) {
                backStack.update {
                    it.copy(navKeys = it.navKeys.apply { removeAt(it.navKeys.lastIndex) })
                }
            }
        }
        println("popUntil ${backStack.value.navKeys}")
    }

    fun clearBackStack() {
        backStack.update { BackStack(navKeys = mutableStateListOf()) }
    }

    fun insertInitialScreen(authState: AuthState) {
        if (authState == AuthState.Authenticated) {
            backStack.update { BackStack(navKeys = mutableStateListOf(HomeNavKey)) }
        } else {
            backStack.update { BackStack(navKeys = mutableStateListOf(AuthenticationNavKey.Login)) }
        }
    }

    private fun <T> SnapshotStateList<T>.addAndReturn(element: T): SnapshotStateList<T> {
        add(element)
        return this
    }
}
