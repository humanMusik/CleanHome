package com.humanmusik.cleanhome.data.network.task

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreTaskApi"
private const val TASKS_FIRESTORE_COLLECTION_ID = "tasks"

interface TaskApi {
    fun listTasks(): Flow<List<Task>>
    fun uploadTask(task: Task)
    fun editTask(task: Task)
}

@Singleton
class FirestoreTaskApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : TaskApi {
    val firestoreTasksCollectionRef = firestore.collection(TASKS_FIRESTORE_COLLECTION_ID)

    override fun listTasks(): Flow<List<Task>> {
        return callbackFlow {
            var ref: CollectionReference? = null

            try {
                ref = firestore.collection(TASKS_FIRESTORE_COLLECTION_ID)
            } catch (e: Throwable) {
                close(e)
            }

            val registration = ref?.addSnapshotListener { snapshot, _ ->
                if (snapshot == null) { return@addSnapshotListener }

                val tasks = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
                trySend(tasks)
            }

            awaitClose { registration?.remove() }
        }
    }

    override fun uploadTask(task: Task) {
        firestoreTasksCollectionRef
            .add(task)
            .addOnSuccessListener {
                Log.d(TAG, "Task uploaded to firestore")
            }
            .addOnFailureListener {
                throw NetworkException("Task failed to be uploaded to firestore")
            }
    }

    override fun editTask(task: Task) {
        val documentRef = requireNotNull(task.id)

        firestoreTasksCollectionRef
            .document(documentRef)
            .set(task)
            .addOnSuccessListener { Log.d(TAG, "Document successfully written!") }
            .addOnFailureListener { throw NetworkException("Failed to edit firestore task") }
    }
}
