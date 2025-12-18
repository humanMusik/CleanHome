package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.domain.model.Home

fun HomeEntity.toHome() = Home(
    id = Home.Id(id),
    name = name,
)

fun List<HomeEntity>.toHomes() = map { it.toHome() }
