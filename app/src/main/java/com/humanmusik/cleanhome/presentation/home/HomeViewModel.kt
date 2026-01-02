package com.humanmusik.cleanhome.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.cleanhome.GetAllHomes
import com.humanmusik.cleanhome.data.repository.cleanhome.GetAllHomes.Companion.invoke
import com.humanmusik.cleanhome.data.repository.preferences.SetHomeIdPreference
import com.humanmusik.cleanhome.data.repository.preferences.SetHomeIdPreference.Companion.invoke
import com.humanmusik.cleanhome.domain.model.Home
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.getOrNull
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllHomes: GetAllHomes,
    private val setHomeIdPreference: SetHomeIdPreference,
) : ViewModel() {
    val state = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = HomeState(homes = FlowState.Loading()),
    )

    init {
        viewModelScope.launch {
            FlowState
                .fromSuspendingFunc {
                    getAllHomes()
                }
                .onEach { flowState ->
                    Log.d("Lezz", "homeVM getAllHomes() flowState: $flowState")
                    state.update { state ->
                        state.copy(
                            homes = flowState
                        )
                    }
                    Log.d("Lezz", "home state: ${state.value.homes.getOrNull()?.size}")
                }
                .onFailure { Log.d("Lezz", "getAllHomes() failed") }
                .launchIn(viewModelScope)
        }
    }

    fun onHomeSelected(
        homeId: Home.Id,
        navigation: () -> Unit,
    ) {
        viewModelScope.launch {
            setHomeIdPreference(homeId)
            navigation()
        }
    }
}
