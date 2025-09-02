package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.ResidentId

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
