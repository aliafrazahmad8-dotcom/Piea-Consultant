package com.piea.student.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.piea.student.data.model.AppVersion
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUpdateRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getLatestVersion(): Resource<AppVersion> {
        return try {
            val doc = firestore.collection(Constants.COLLECTION_APP_CONFIG)
                .document(Constants.APP_VERSION_DOC_ID)
                .get().await()
            val version = doc.toObject(AppVersion::class.java) ?: AppVersion()
            Resource.Success(version)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not check for updates.")
        }
    }

    /** Admin-only: publishes a new available version so all students get notified. */
    suspend fun publishUpdate(version: AppVersion): Resource<Unit> {
        return try {
            firestore.collection(Constants.COLLECTION_APP_CONFIG)
                .document(Constants.APP_VERSION_DOC_ID)
                .set(version).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not publish update info.")
        }
    }
}
