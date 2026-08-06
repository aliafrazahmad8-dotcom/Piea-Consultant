package com.piea.student.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.piea.student.data.model.NotificationItem
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getMyNotifications(): Resource<List<NotificationItem>> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_NOTIFICATIONS)
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(NotificationItem::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load notifications.")
        }
    }

    suspend fun markAsRead(notificationId: String) {
        try {
            firestore.collection(Constants.COLLECTION_NOTIFICATIONS)
                .document(notificationId)
                .update("read", true)
                .await()
        } catch (_: Exception) {
            // Non-critical, ignore.
        }
    }
}
