package com.example.cleanhome.data.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Room

data class RoomWithResidents(
    @Embedded val room: Room,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "residentId",
        associateBy = Junction(ResidentRoomCrossRef::class)
    )
    val residents: List<Resident>,
)