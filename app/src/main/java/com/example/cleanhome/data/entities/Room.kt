package com.example.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Room(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int? = null,
    val roomName: String,
    val houseId: Int,
)