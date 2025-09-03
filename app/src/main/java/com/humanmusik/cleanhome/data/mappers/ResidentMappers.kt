package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.domain.model.Resident

fun ResidentEntity.toResident() = Resident(
    id = Resident.Id(id),
    name = name,
)

fun List<ResidentEntity>.toResidents() =
    map { it.toResident() }

fun Resident.toResidentEntity() = ResidentEntity(
    id = id?.value ?: 0,
    name = name,
)
