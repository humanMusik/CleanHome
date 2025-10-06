package com.humanmusik.cleanhome.data.api.room

import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import com.humanmusik.cleanhome.data.api.task.Task
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreRoomsApi"
private const val ROOMS_FIRESTORE_COLLECTION_ID = "rooms"

interface RoomApi {
    fun listRooms(): Flow<List<Room>>
    fun uploadTask(task: Task)
    fun editTask(task: Task)
}

@Singleton
class FirestoreRoomApi @Inject constructor(
    firestore: FirebaseFirestore,
) : RoomApi {
    val firestoreRoomsCollectionRef = firestore.collection(ROOMS_FIRESTORE_COLLECTION_ID)

    override fun listRooms(): Flow<List<Room>> {
        return callbackFlow {
            val registration = firestoreRoomsCollectionRef
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        cancel(
                            "Error fetching tasks",
                            NetworkException("Failed to fetch rooms from firestore")
                        )
                        return@addSnapshotListener
                    }

                    val rooms = snapshot?.documents?.mapNotNull { it.toObject(Room::class.java) }
                        ?: emptyList()
                    trySend(rooms)
                }

            awaitClose { registration.remove() }
        }
    }

    override fun uploadTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }
}