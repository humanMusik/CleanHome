package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.domain.model.Home

fun HomeEntity.toHouse() = Home(
    id = id,
    name = name,
)