package com.humanmusik.cleanhome.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

const val ROOM_ID = "id"

@Entity
data class RoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val homeId: Int,
)

data class RoomWithMetadata(
    @Embedded val room: RoomEntity,
    @Relation(
        parentColumn = ROOM_ID,
        entityColumn = RESIDENT_ID,
        associateBy = Junction(
            value = ResidentRoomCrossRef::class,
            parentColumn = ROOM_ID_CROSS_REF,
            entityColumn = RESIDENT_ID_CROSS_REF,
        ),
    )
    val residents: List<ResidentEntity>,
)