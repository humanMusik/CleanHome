package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    val id: RoomId,
    val name: String,
    val homeId: Int,
) : Parcelable

@JvmInline
@Parcelize
value class RoomId(val value: Int?) : Parcelable
