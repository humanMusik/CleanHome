package com.example.cleanhome.data.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Room

data class ResidentWithRooms(
    @Embedded val resident: Resident,
    @Relation(
        parentColumn = "residentId",
        entityColumn = "roomId",
        associateBy = Junction(ResidentRoomCrossRef::class)
    )
    val rooms: List<Room>,
)