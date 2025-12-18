package com.humanmusik.cleanhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.auth.AuthRepository
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncRooms
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncRooms.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val syncTasks: SyncTasks,
    private val syncRooms: SyncRooms,
    private val checkUserSession: CheckUserSession,
) : ViewModel() {

    val userSession: MutableSavedStateFlow<AuthState> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = AuthState.Loading,
    )

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        checkUserSession
            .invoke()
            .onEach { userSession.update { it } }
            .launchIn(viewModelScope)
    }

    fun syncRooms() {
        viewModelScope.launch {
            syncRooms.invoke()
        }
    }

    fun syncTasks() {
        viewModelScope.launch {
            syncTasks.invoke()
        }
    }
}
