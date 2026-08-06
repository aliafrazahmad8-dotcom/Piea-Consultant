package com.piea.student.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.piea.student.data.model.University
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniversityRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getUniversities(): Resource<List<University>> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_UNIVERSITIES).get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(University::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load universities.")
        }
    }

    suspend fun getUniversityById(id: String): Resource<University> {
        return try {
            val doc = firestore.collection(Constants.COLLECTION_UNIVERSITIES).document(id).get().await()
            val university = doc.toObject(University::class.java)?.copy(id = doc.id)
                ?: return Resource.Error("University not found.")
            Resource.Success(university)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load university.")
        }
    }
}
