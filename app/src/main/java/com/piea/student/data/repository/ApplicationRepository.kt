package com.piea.student.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.piea.student.data.model.Application
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun submitApplication(application: Application): Resource<String> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val docRef = firestore.collection(Constants.COLLECTION_APPLICATIONS).document()
            val finalApp = application.copy(id = docRef.id, userId = uid, status = Constants.STATUS_SUBMITTED)
            docRef.set(finalApp).await()
            Resource.Success(docRef.id)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not submit application.")
        }
    }

    suspend fun getMyApplications(): Resource<List<Application>> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_APPLICATIONS)
                .whereEqualTo("userId", uid)
                .orderBy("submittedAt", Query.Direction.DESCENDING)
                .get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(Application::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load your applications.")
        }
    }
}
