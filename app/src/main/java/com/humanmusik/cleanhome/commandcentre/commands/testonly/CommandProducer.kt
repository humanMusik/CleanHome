package com.humanmusik.cleanhome.commandcentre.commands.testonly

//import com.humanmusik.cleanhome.data.api.task.SnapshotListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//interface CommandProducer {
//    fun flowOfCommands(): Flow<CommandId>
//}
//
//class DefaultCommandProducer(
//    private val firestoreSnapshotListener: SnapshotListener,
//) : CommandProducer {
//    override fun flowOfCommands(): Flow<CommandId> =
//        flowOfTaskUpdatedCommands()
//
//    private fun flowOfTaskUpdatedCommands() =
//        firestoreSnapshotListener
//            .onTaskDataReceived()
//            .map { CommandId.taskUpdate }
//
//}
