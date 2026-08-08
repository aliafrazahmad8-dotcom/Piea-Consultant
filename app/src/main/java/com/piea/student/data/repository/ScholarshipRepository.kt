package com.piea.student.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.piea.student.data.model.Scholarship
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScholarshipRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getScholarships(): Resource<List<Scholarship>> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_SCHOLARSHIPS).get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(Scholarship::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load scholarships.")
        }
    }

    suspend fun addScholarship(scholarship: Scholarship): Resource<Unit> {
        return try {
            val docRef = firestore.collection(Constants.COLLECTION_SCHOLARSHIPS).document()
            docRef.set(scholarship.copy(id = docRef.id)).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not add scholarship.")
        }
    }
}
