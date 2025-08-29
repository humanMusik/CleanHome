@file:OptIn(ExperimentalForInheritanceCoroutinesApi::class)
package com.humanmusik.cleanhome.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class SavedStateFlow<State> internal constructor(
    internal val stateFlow: StateFlow<State>,
) : StateFlow<State> by stateFlow

class MutableSavedStateFlow<State>(
    private val mutableStateFlow: MutableStateFlow<State>
) : SavedStateFlow<State>(mutableStateFlow), MutableStateFlow<State> by mutableStateFlow {
    override val replayCache: List<State> by mutableStateFlow::replayCache
    override var value: State by mutableStateFlow::value

    override suspend fun collect(collector: FlowCollector<State>): Nothing {
        stateFlow.collect(collector)
    }
}

fun <State : Any> ViewModel.savedStateFlow(
    savedStateBehaviour: SavedStateBehaviour<State>,
    initialState: State,
): MutableSavedStateFlow<State> {
    val flow = savedStateBehaviour.createSaveBehaviour(initialState)
    return MutableSavedStateFlow(flow)
}