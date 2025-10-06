package com.humanmusik.cleanhome.data.api.room

import com.google.firebase.firestore.DocumentId

data class Room(
    @DocumentId val id: String? = null,
    val name: String? = null
)
