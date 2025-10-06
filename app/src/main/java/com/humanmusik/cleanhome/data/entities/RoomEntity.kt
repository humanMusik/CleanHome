package com.humanmusik.cleanhome.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val ROOM_ENTITY_ID = "id"

@Entity
data class RoomEntity(
    @PrimaryKey
    @ColumnInfo(ROOM_ENTITY_ID) val id: String,
    val name: String,
)
