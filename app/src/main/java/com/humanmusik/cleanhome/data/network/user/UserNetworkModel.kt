package com.humanmusik.cleanhome.data.network.user

import com.google.firebase.firestore.DocumentId
import com.humanmusik.cleanhome.domain.model.Home

data class User(
    @DocumentId val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val homes: List<String>? = null,
)