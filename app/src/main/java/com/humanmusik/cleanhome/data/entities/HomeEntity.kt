package com.humanmusik.cleanhome.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

const val HOME_ID = "id"

@Entity
data class HomeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)

data class HomeWithMetadata(
    @Embedded val home: HomeEntity,
    @Relation(
        parentColumn = HOME_ID,
        entityColumn = "homeId",
    )
    val rooms: List<RoomEntity>,
    @Relation(
        parentColumn = HOME_ID,
        entityColumn = "homeId",
    )
    val residents: List<ResidentEntity>,
)