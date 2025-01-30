package com.example.cleanhome.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Task

data class ResidentWithTasks(
    @Embedded val resident: Resident,
    @Relation(
        parentColumn = "residentId",
        entityColumn = "assignedTo",
    )
    val tasks: List<Task>
)
