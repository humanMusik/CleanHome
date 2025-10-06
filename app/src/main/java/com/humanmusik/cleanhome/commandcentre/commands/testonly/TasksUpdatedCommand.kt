package com.humanmusik.cleanhome.commandcentre.commands.testonly

import com.humanmusik.cleanhome.data.repository.SyncTasks
import com.humanmusik.cleanhome.data.repository.SyncTasks.Companion.invoke

class TasksUpdatedCommand(
    private val syncTasks: SyncTasks,
) : Command {
    override val id: CommandId
        get() = CommandId.taskUpdate

    override suspend fun execute() {
        syncTasks()
    }
}