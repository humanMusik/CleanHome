package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.domain.model.Room

fun RoomEntity.toRoom() = Room(
    id = id,
    name = name,
)
