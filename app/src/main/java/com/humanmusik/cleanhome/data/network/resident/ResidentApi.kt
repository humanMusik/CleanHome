package com.humanmusik.cleanhome.data.network.resident

import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import com.humanmusik.cleanhome.domain.model.Home
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ResidentApi {
    suspend fun listResidents(homeId: Home.Id): List<Resident>
    suspend fun uploadResident(resident: Resident)
    suspend fun editResident(resident: Resident)
}

class FirestoreResidentApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ResidentApi {
    override suspend fun listResidents(homeId: Home.Id): List<Resident> {
        return try {
            val documents = firestore
                .collection(getResidentFirestoreCollectionRef(homeId))
                .get()
                .await()

            documents.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Resident::class.java)
            }
        } catch (e: Throwable) {
            throw NetworkException(e.message ?: "unknown network error occurred")
        }
    }

    override suspend fun uploadResident(resident: Resident) {
        TODO("Not yet implemented")
    }

    override suspend fun editResident(resident: Resident) {
        TODO("Not yet implemented")
    }

    companion object {
        fun getResidentFirestoreCollectionRef(homeId: Home.Id) = "home/${homeId.value}/residents"
    }
}