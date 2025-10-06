package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    val id: Id?,
    val name: String,
) : Parcelable {
    @JvmInline
    @Parcelize
    value class Id(val value: String) : Parcelable
}
