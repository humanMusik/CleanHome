package com.humanmusik.cleanhome.data.network.task

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import com.humanmusik.cleanhome.domain.model.Home
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreTaskApi"

interface TaskApi {
    suspend fun listTasks(homeId: Home.Id): List<Task>
    suspend fun uploadTask(homeId: Home.Id, task: Task)
    suspend fun editTask(homeId: Home.Id, task: Task)
    suspend fun deleteTask(homeId: Home.Id, taskId: String)
}

@Singleton
class FirestoreTaskApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) : TaskApi {
    override suspend fun listTasks(homeId: Home.Id): List<Task> {
        return try {
            val documents = firestore
                .collection(getTaskFirestoreCollectionRef(homeId))
                .get()
                .await()

            documents.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Task::class.java)
            }
        } catch (e: Throwable) {
            throw NetworkException(e.message ?: "unknown network error occurred")
        }
    }

    override suspend fun uploadTask(homeId: Home.Id, task: Task) {
        val documentId = task.id ?: throw IllegalStateException()
        firestore.collection(getTaskFirestoreCollectionRef(homeId))
            .document(documentId)
            .set(task)
            .addOnSuccessListener {
                Log.d(TAG, "Task uploaded to firestore")
            }
            .addOnFailureListener {
                throw NetworkException("Task failed to be uploaded to firestore")
            }
    }

    override suspend fun editTask(homeId: Home.Id, task: Task) {
        val documentRef = requireNotNull(task.id)

        firestore.collection(getTaskFirestoreCollectionRef(homeId))
            .document(documentRef)
            .set(task)
            .addOnSuccessListener { Log.d(TAG, "Document successfully written!") }
            .addOnFailureListener { throw NetworkException("Failed to edit firestore task") }
    }

    override suspend fun deleteTask(homeId: Home.Id, taskId: String) {
        firestore.collection(getTaskFirestoreCollectionRef(homeId))
            .document(taskId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "Document successfully deleted") }
            .addOnFailureListener { throw NetworkException("Failed to delete firestore task") }
    }

    companion object {
        fun getTaskFirestoreCollectionRef(homeId: Home.Id) = "home/${homeId.value}/tasks"
    }
}
