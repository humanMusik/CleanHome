package com.humanmusik.cleanhome.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

const val ROOM_ENTITY_ID = "id"

@Entity
data class RoomEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(ROOM_ENTITY_ID) val id: Int = 0,
    val name: String,
)
