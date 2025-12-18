package com.humanmusik.cleanhome.commandcentre.commands.testonly

import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks.Companion.invoke

class TasksUpdatedCommand(
    private val syncTasks: SyncTasks,
) : Command {
    override val id: CommandId
        get() = CommandId.taskUpdate

    override suspend fun execute() {
        syncTasks()
    }
}