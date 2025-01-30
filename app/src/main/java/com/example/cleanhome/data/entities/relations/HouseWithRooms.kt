package com.example.cleanhome.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cleanhome.data.entities.House
import com.example.cleanhome.data.entities.Room

data class HouseWithRooms(
    @Embedded val house: House,
    @Relation(
        parentColumn = "houseId",
        entityColumn = "houseId",
    )
    val rooms: List<Room>,
)