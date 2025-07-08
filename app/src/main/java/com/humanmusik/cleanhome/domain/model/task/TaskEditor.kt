package com.humanmusik.cleanhome.domain.model.task

import android.icu.util.Calendar
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TaskEditor {
    fun reassignTask(task: Task): Task
}

class TaskEditorImpl @Inject constructor(
    private val flowOfAllResidents: FlowOfAllResidents,
    private val flowOfTasksForResident: FlowOfTasksForResident,
) : TaskEditor {
    override fun reassignTask(task: Task): Task {
        TODO("Not yet implemented")
    }

}