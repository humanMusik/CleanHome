package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Resident(
    val id: ResidentId,
    val name: String,
    val homeId: Int,
) : Parcelable

@JvmInline
@Parcelize
value class ResidentId(val value: Int?) : Parcelable
