package com.humanmusik.cleanhome.data.network.task

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import com.humanmusik.cleanhome.domain.model.Home
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreTaskApi"

interface TaskApi {
    fun listTasks(homeId: Home.Id): Flow<List<Task>>
    fun uploadTask(homeId: Home.Id, task: Task)
    fun editTask(homeId: Home.Id, task: Task)
}

@Singleton
class FirestoreTaskApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : TaskApi {
    override fun listTasks(homeId: Home.Id): Flow<List<Task>> {
        return callbackFlow {
            var ref: CollectionReference? = null

            try {
                ref = firestore.collection(getTaskFirestoreCollectionRef(homeId))
            } catch (e: Throwable) {
                close(e)
            }

            val registration = ref?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    return@addSnapshotListener
                }

                val tasks = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
                trySend(tasks)
            }

            awaitClose { registration?.remove() }
        }
    }

    override fun uploadTask(homeId: Home.Id, task: Task) {
        firestore.collection(getTaskFirestoreCollectionRef(homeId))
            .add(task)
            .addOnSuccessListener {
                Log.d(TAG, "Task uploaded to firestore")
            }
            .addOnFailureListener {
                throw NetworkException("Task failed to be uploaded to firestore")
            }
    }

    override fun editTask(homeId: Home.Id, task: Task) {
        val documentRef = requireNotNull(task.id)

        firestore.collection(getTaskFirestoreCollectionRef(homeId))
            .document(documentRef)
            .set(task)
            .addOnSuccessListener { Log.d(TAG, "Document successfully written!") }
            .addOnFailureListener { throw NetworkException("Failed to edit firestore task") }
    }

    companion object {
        fun getTaskFirestoreCollectionRef(homeId: Home.Id) = "home/${homeId.value}/tasks"
    }
}
