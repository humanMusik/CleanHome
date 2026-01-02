package com.humanmusik.cleanhome.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

const val RESIDENT_ENTITY_ID = "id"

@Entity
data class ResidentEntity(
    @PrimaryKey
    @ColumnInfo(RESIDENT_ENTITY_ID) val id: String,
    val name: String,
)
