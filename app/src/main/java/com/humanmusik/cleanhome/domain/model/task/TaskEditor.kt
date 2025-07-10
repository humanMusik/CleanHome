package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents.Companion.invoke
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident.Companion.invoke
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration

interface TaskEditor {
    fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    ): Task
}

class TaskEditorImpl @Inject constructor(
    private val flowOfAllResidents: FlowOfAllResidents,
    private val flowOfTasksForResident: FlowOfTasksForResident,
) : TaskEditor {
    override fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    ): Task {
        return flowOfAllResidents().map { residents ->
            residents.ma
        }


        Task(
            id = task.id,
            name = task.name,
            room = task.room,
            duration = task.duration,
            frequency = task.frequency,
            scheduledDate = /* TODO */,
            urgency = task.urgency,
            assignedTo = /* TODO */,
        )
    }

}