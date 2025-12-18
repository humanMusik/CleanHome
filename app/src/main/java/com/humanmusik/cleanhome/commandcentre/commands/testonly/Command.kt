package com.humanmusik.cleanhome.commandcentre.commands.testonly

interface Command {
    val id: CommandId
    suspend fun execute()
}

@JvmInline
value class CommandId(val value: String) {
    companion object {
        val taskUpdate = CommandId("task_update")
    }
}