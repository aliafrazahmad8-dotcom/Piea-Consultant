package com.piea.student.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.piea.student.data.model.Program
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgramRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPrograms(): Resource<List<Program>> {
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_PROGRAMS).get().await()
            val list = snapshot.documents.mapNotNull { it.toObject(Program::class.java)?.copy(id = it.id) }
            Resource.Success(list)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load programs.")
        }
    }
}
