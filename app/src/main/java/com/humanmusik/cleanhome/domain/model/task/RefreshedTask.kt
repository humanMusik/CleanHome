package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.model.Resident

class ReassignedTask(
    val completedTask: Task,
    val residents: Resident,
) {
    // TODO: Add a capacity property to resident
}