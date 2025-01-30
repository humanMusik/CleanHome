package com.example.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Resident(
    @PrimaryKey(autoGenerate = true)
    val residentId: Int? = null,
    val residentName: String,
    val houseId: Int,
)