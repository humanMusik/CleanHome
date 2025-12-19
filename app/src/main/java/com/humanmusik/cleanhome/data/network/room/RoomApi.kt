package com.humanmusik.cleanhome.data.network.room

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.network.task.Task
import com.humanmusik.cleanhome.domain.model.Home
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreRoomsApi"
private const val ROOMS_FIRESTORE_COLLECTION_ID = "rooms"
// TODO Requesting for Rooms/Tasks is more complicated now, now that we need
// to grab the home id. 

interface RoomApi {
    fun listRooms(homeId: Home.Id): Flow<List<Room>>
    fun uploadRoom()
    fun editRoom()
}

@Singleton
class FirestoreRoomApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RoomApi {
    override fun listRooms(homeId: Home.Id): Flow<List<Room>> {
        return callbackFlow {
            var ref: CollectionReference? = null

            try {
                ref = firestore.collection(getRoomFirestoreCollectionRef(homeId))
            } catch (e: Throwable) {
                close(e)
            }

            val registration = ref?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) { return@addSnapshotListener }

                val rooms = snapshot.documents.mapNotNull { it.toObject(Room::class.java) }
                trySend(rooms)
            }

            awaitClose { registration?.remove() }
        }
    }

    override fun uploadRoom() {
        TODO("Not yet implemented")
    }

    override fun editRoom() {
        TODO("Not yet implemented")
    }

    companion object {
        private fun getRoomFirestoreCollectionRef(homeId: Home.Id) = "home/${homeId.value}/rooms"
    }
}