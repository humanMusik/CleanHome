package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
) : Parcelable
