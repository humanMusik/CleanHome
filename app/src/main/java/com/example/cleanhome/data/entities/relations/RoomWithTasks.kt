package com.example.cleanhome.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cleanhome.data.entities.Room
import com.example.cleanhome.data.entities.Task

class RoomWithTasks(
    @Embedded val room: Room,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "roomId",
    )
    val tasks: List<Task>,
)