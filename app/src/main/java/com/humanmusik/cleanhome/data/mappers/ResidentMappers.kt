package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.domain.model.Resident

fun ResidentEntity.toResident() = Resident(
    id = id,
    name = name,
)
