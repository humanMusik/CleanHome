package com.humanmusik.cleanhome.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val HOME_ID = "id"

@Entity
data class HomeEntity(
    @PrimaryKey
    @ColumnInfo(HOME_ID) val id: String,
    val name: String,
)
