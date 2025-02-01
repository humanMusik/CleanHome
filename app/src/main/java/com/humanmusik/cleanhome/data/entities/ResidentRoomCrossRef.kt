package com.humanmusik.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey

const val RESIDENT_ID_CROSS_REF = "residentId"
const val ROOM_ID_CROSS_REF = "roomId"

@Entity(
    primaryKeys = [RESIDENT_ID_CROSS_REF, ROOM_ID_CROSS_REF],
    foreignKeys = [
        ForeignKey(
            entity = ResidentEntity::class,
            parentColumns = [RESIDENT_ID],
            childColumns = [RESIDENT_ID_CROSS_REF],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = [ROOM_ID],
            childColumns = [ROOM_ID_CROSS_REF],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
data class ResidentRoomCrossRef(
    val residentId: Int,
    val roomId: Int,
)