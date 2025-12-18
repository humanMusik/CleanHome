package com.humanmusik.cleanhome.data.network.home

import com.google.firebase.firestore.DocumentId

data class Home(
    @DocumentId val id: String? = null,
    val name: String? = null,
)
