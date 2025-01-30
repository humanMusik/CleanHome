package com.example.cleanhome.data.entities.relations

import androidx.room.Entity

const val RESIDENT_ID = "residentId"
const val ROOM_ID = "roomId"

@Entity(primaryKeys = [RESIDENT_ID, ROOM_ID])
data class ResidentRoomCrossRef(
    val residentId: Int,
    val roomId: Int,
)