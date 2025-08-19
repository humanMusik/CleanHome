package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Resident(
    val id: Int,
    val name: String,
    val homeId: Int,
) : Parcelable
