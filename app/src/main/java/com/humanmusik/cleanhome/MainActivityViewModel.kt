package com.humanmusik.cleanhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession.Companion.invoke
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import com.humanmusik.cleanhome.workers.SyncDataWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val checkUserSession: CheckUserSession,
    private val workManager: WorkManager,
) : ViewModel() {

    val userSession: MutableSavedStateFlow<AuthState> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = AuthState.Loading,
    )

    init {
        checkUserSession()
        setupRecurringSync()
    }

    private fun checkUserSession() {
        checkUserSession
            .invoke()
            .onEach { userSession.update { it } }
            .launchIn(viewModelScope)
    }

    private fun setupRecurringSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncDataWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        ).setConstraints(constraints).build()

        if (userSession.value is AuthState.Authenticated) {
            workManager.enqueueUniquePeriodicWork(
                uniqueWorkName = "SYNC_DATA_WORK",
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP, // Keep the existing work if it's already scheduled
                request = syncRequest,
            )
        }
    }
}
