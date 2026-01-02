package com.humanmusik.cleanhome.data.network.task

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String? = null,
    val name: String? = null,
    val roomId: String? = null,
    val duration: String? = null,
    val scheduledDate: String? = null,
    val frequency: String? = null,
    val urgent: Boolean? = null,
    val assigneeId: String? = null,
    val state: String? = null,
    val lastCompletedDate: String? = null,
)
