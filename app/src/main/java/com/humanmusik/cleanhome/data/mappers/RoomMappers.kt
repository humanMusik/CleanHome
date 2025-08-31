package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.RoomId

fun RoomEntity.toRoom() = Room(
    id = RoomId(id),
    name = name,
    homeId = homeId,
)

fun Room.toRoomEntity() = RoomEntity(
    id = id.value ?: 0,
    name = name,
    homeId = homeId,
)
