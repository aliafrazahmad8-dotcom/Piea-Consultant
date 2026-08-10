package com.piea.student.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.piea.student.data.model.SupportMessage
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupportRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun submitMessage(type: String, subject: String, message: String): Resource<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: ""
            val docRef = firestore.collection(Constants.COLLECTION_SUPPORT_MESSAGES).document()
            val supportMessage = SupportMessage(
                id = docRef.id,
                userId = uid,
                type = type,
                subject = subject,
                message = message
            )
            docRef.set(supportMessage).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not submit. Please try again.")
        }
    }
}
