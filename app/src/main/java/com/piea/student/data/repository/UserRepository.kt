package com.piea.student.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.piea.student.data.model.User
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun getCurrentUserProfile(): Resource<User> {
        val uid = auth.currentUser?.uid ?: return Resource.Error("Not signed in.")
        return try {
            val snapshot = firestore.collection(Constants.COLLECTION_USERS).document(uid).get().await()
            val user = snapshot.toObject(User::class.java) ?: User(uid = uid, email = auth.currentUser?.email ?: "")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not load profile.")
        }
    }

    suspend fun updateProfile(user: User): Resource<Unit> {
        return try {
            firestore.collection(Constants.COLLECTION_USERS).document(user.uid).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not update profile.")
        }
    }

    suspend fun uploadProfilePhoto(uid: String, bytes: ByteArray): Resource<String> {
        return try {
            val ref = storage.reference.child("${Constants.STORAGE_PROFILE_PHOTOS}/$uid/${UUID.randomUUID()}.jpg")
            ref.putBytes(bytes).await()
            val url = ref.downloadUrl.await().toString()
            Resource.Success(url)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Photo upload failed.")
        }
    }
}
