package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.domain.model.Room

fun RoomEntity.toRoom() = Room(
    id = Room.Id(id),
    name = name,
)

fun Room.toRoomEntity() = RoomEntity(
    id = requireNotNull(id?.value) { "Room Id cannot be null" },
    name = name,
)
