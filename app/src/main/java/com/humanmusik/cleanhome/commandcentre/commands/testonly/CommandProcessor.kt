package com.humanmusik.cleanhome.commandcentre.commands.testonly

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CommandProcessor {
    fun enqueue(command: Command): Boolean
    suspend fun execute(command: Command)
}

class DefaultCommandProcessor : CommandProcessor {
    private val processingStateFlow = MutableStateFlow(
        ProcessingState(
            queuedCommands = emptySet(),
            executingCommands = emptySet(),
        )
    )

    override fun enqueue(command: Command): Boolean {
        var enqueued = false

        processingStateFlow.update { processingState ->
            if (processingState.queuedCommands.any { it.id == command.id }) {
                processingState
            } else {
                enqueued = true
                processingState.copy(
                    queuedCommands = processingState.queuedCommands + command
                )
            }
        }

        return enqueued
    }

    override suspend fun execute(command: Command) {
        processingStateFlow.update { processingState ->
            processingState.copy(
                queuedCommands = processingState.queuedCommands - command,
                executingCommands = processingState.executingCommands + command,
            )
        }

        command.execute()

        processingStateFlow.update { processingState ->
            processingState.copy(
                executingCommands = processingState.executingCommands - command,
            )
        }
    }

    private data class ProcessingState(
        val queuedCommands: Set<Command>,
        val executingCommands: Set<Command>,
    )
}