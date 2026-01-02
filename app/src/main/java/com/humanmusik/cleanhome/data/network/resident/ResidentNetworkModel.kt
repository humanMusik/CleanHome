package com.humanmusik.cleanhome.data.network.resident

import com.google.firebase.firestore.DocumentId

data class Resident(
    @DocumentId val userId: String? = null,
    val name: String? = null,
)