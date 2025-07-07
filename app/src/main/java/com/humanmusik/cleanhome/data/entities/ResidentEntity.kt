package com.humanmusik.cleanhome.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

const val RESIDENT_ID = "id"

@Entity
data class ResidentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val homeId: Int,
)

data class ResidentWithMetadata(
    @Embedded val resident: ResidentEntity,
    @Relation(
        parentColumn = RESIDENT_ID,
        entityColumn = ROOM_ID,
        associateBy = Junction(
            value = ResidentRoomCrossRef::class,
            parentColumn = RESIDENT_ID_CROSS_REF,
            entityColumn = ROOM_ID_CROSS_REF,
        ),
    )
    val rooms: List<RoomEntity>,
)