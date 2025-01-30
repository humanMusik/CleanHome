package com.example.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class House(
    @PrimaryKey(autoGenerate = true)
    val houseId: Int? = null,
    val houseName: String,
)