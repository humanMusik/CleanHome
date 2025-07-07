package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.model.Resident

interface TaskEditor {
    fun reassignTask(task: Task): Task
}

class TaskEditorImpl(
    val allResidents: List<Resident>,
) : TaskEditor {
    override fun reassignTask(task: Task): Task {
        TODO("Not yet implemented")
    }

}