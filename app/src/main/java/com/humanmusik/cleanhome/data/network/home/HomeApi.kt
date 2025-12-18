package com.humanmusik.cleanhome.data.network.home

import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.humanmusik.cleanhome.domain.model.Home as HomeDomain

private const val HOME_FIRESTORE_COLLECTION_ID = "home"

interface HomeApi {
    suspend fun getHome(homeId: HomeDomain.Id): Home
}

class FirestoreHomeApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : HomeApi {
    override suspend fun getHome(homeId: HomeDomain.Id): Home {
        return try {
            val document = firestore
                .collection(HOME_FIRESTORE_COLLECTION_ID)
                .document(homeId.value)
                .get()
                .await()

            document.toObject(Home::class.java) ?: throw IllegalStateException()
        } catch (e: Throwable) {
            if (e is IllegalStateException) {
                throw e
            } else {
                throw NetworkException(e.message ?: "Unexpected network error occurred")
            }
        }
    }
}