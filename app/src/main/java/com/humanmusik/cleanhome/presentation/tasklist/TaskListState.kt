package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import java.time.OffsetDateTime

data class TaskListState(
    val tasks: List<Task> = listOf(
        Task(
            id = 1,
            name = "me",
            room = Room(1, "My Room"),
            duration = 1000L,
            frequency = Frequency.Weekly,
            scheduledDate = OffsetDateTime.now(),
            urgency = Urgency.Urgent,
            assignedTo = Resident(1, "me")
        ),
    ),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
)
